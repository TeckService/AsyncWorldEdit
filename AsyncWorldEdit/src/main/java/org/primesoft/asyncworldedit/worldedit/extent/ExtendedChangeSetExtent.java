/*
 * AsyncWorldEdit a performance improvement plugin for Minecraft WorldEdit plugin.
 * Copyright (c) 2016, SBPrime <https://github.com/SBPrime/>
 * Copyright (c) AsyncWorldEdit contributors
 *
 * All rights reserved.
 *
 * Redistribution in source, use in source and binary forms, with or without
 * modification, are permitted free of charge provided that the following 
 * conditions are met:
 *
 * 1.  Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 * 2.  Redistributions of source code, with or without modification, in any form
 *     other then free of charge is not allowed,
 * 3.  Redistributions of source code, with tools and/or scripts used to build the 
 *     software is not allowed,
 * 4.  Redistributions of source code, with information on how to compile the software
 *     is not allowed,
 * 5.  Providing information of any sort (excluding information from the software page)
 *     on how to compile the software is not allowed,
 * 6.  You are allowed to build the software for your personal use,
 * 7.  You are allowed to build the software using a non public build server,
 * 8.  Redistributions in binary form in not allowed.
 * 9.  The original author is allowed to redistrubute the software in bnary form.
 * 10. Any derived work based on or containing parts of this software must reproduce
 *     the above copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided with the
 *     derived work.
 * 11. The original author of the software is allowed to change the license
 *     terms or the entire license of the software as he sees fit.
 * 12. The original author of the software is allowed to sublicense the software
 *     or its parts using any license terms he sees fit.
 * 13. By contributing to this project you agree that your contribution falls under this
 *     license.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.primesoft.asyncworldedit.worldedit.extent;

import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.ChangeSetExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.history.change.EntityCreate;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.biome.BaseBiome;
import javax.annotation.Nullable;
import org.primesoft.asyncworldedit.api.worldedit.ICancelabeEditSession;
import org.primesoft.asyncworldedit.worldedit.history.change.BiomeChange;
import org.primesoft.asyncworldedit.worldedit.history.changeset.IExtendedChangeSet;

/**
 * This class is based on WorldEdit ChangeSetExtent
 *
 * @author SBPrime
 */
public class ExtendedChangeSetExtent extends ChangeSetExtent {

    private final IExtendedChangeSet m_changeSet;
    private final ICancelabeEditSession m_cancelableEditSession;

    public ExtendedChangeSetExtent(ICancelabeEditSession editSession, Extent extent, IExtendedChangeSet changeSet) {
        super(extent, new ProxyChangeSet(changeSet, editSession));

        m_changeSet = changeSet;
        m_cancelableEditSession = editSession;
    }

    @Override
    public boolean setBiome(BlockVector2 position, BaseBiome biome) {
        BaseBiome previous = getBiome(position);

        try {
            m_changeSet.addExtended(new BiomeChange(position, previous, biome), m_cancelableEditSession);
        } catch (WorldEditException ex) {
            return false;
        }

        return super.setBiome(position, biome);
    }

    /*@Override
    public boolean setBlock(Vector3 location, BlockStateHolder block) throws WorldEditException {
        BaseBlock previous = getFullBlock(location);
        m_changeSet.addExtended(new BlockChange(location.toBlockVector(), previous, block), m_cancelableEditSession);
        return super.setBlock(location, block);
    }*/

    @Nullable
    @Override
    public Entity createEntity(Location location, BaseEntity state) {
        Entity entity = super.createEntity(location, state);
        if (state != null) {
            try {
                m_changeSet.addExtended(new EntityCreate(location, state, entity), m_cancelableEditSession);
            } catch (WorldEditException ex) {
                return null;
            }
        }
        return entity;
    }

}
