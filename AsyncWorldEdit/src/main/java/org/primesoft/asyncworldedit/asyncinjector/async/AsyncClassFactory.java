/*
 * AsyncWorldEdit a performance improvement plugin for Minecraft WorldEdit plugin.
 * Copyright (c) 2014, SBPrime <https://github.com/SBPrime/>
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
package org.primesoft.asyncworldedit.asyncinjector.async;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import java.util.UUID;
import org.primesoft.asyncworldedit.api.inner.IAsyncWorldEditCore;
import org.primesoft.asyncworldedit.api.playerManager.IPlayerEntry;
import org.primesoft.asyncworldedit.api.playerManager.IPlayerManager;
import org.primesoft.asyncworldedit.core.AwePlatform;
import org.primesoft.asyncworldedit.injector.classfactory.IJobProcessor;
import org.primesoft.asyncworldedit.injector.classfactory.IOperationProcessor;
import org.primesoft.asyncworldedit.injector.classfactory.base.BaseClassFactory;
import org.primesoft.asyncworldedit.utils.ExceptionHelper;
import org.primesoft.asyncworldedit.worldedit.extent.clipboard.BiomeClipboard;
import org.primesoft.asyncworldedit.worldedit.function.CascadeRegionFunction;
import org.primesoft.asyncworldedit.worldedit.function.biome.ExtentBiomeCopy;
import org.primesoft.asyncworldedit.worldedit.world.AsyncWorld;

/**
 *
 * @author SBPrime
 */
public class AsyncClassFactory extends BaseClassFactory {

    /**
     * The operation processor
     */
    private final AsyncOperationProcessor m_operationProcessor;
    
    private final AsyncJobProcessor m_jobProcessor;
    
    public AsyncClassFactory(IAsyncWorldEditCore aweCore)
    {        
        m_operationProcessor = new AsyncOperationProcessor(aweCore);
        
        m_jobProcessor = new AsyncJobProcessor(aweCore);
    }
    
    @Override
    public IOperationProcessor getOperationProcessor() {
        return m_operationProcessor;
    }
    
    @Override
    public IJobProcessor getJobProcessor() {
        return m_jobProcessor;
    }

    @Override
    public Clipboard createClipboard(Clipboard c, Region region) {
        return new BiomeClipboard(c, region);
    }

    
    
    /*@Override
    public Clipboard createClipboard(Region region) {
        return new BiomeClipboard(region);
    }*/

    @Override
    public RegionFunction addBiomeCopy(RegionFunction blockCopy, 
            Extent source, BlockVector3 from, Extent destination, BlockVector3 to, 
            Transform currentTransform, boolean singleSet) {
        ExtentBiomeCopy bc = new ExtentBiomeCopy(source, from, destination, to, currentTransform, singleSet);        
        
        return new CascadeRegionFunction(blockCopy, bc);
    }

    @Override
    public void handleError(WorldEditException ex, String name) {
        ExceptionHelper.printException(ex, String.format("Error while processing async operation %1$s", name));
    }
    
    @Override
    public IPlayerEntry getPlayer(UUID uniqueId) {
        IPlayerManager pm = AwePlatform.getInstance().getCore().getPlayerManager();
        return  pm.getPlayer(uniqueId);
    }

    @Override
    public World wrapWorld(World world, IPlayerEntry player) {
        return AsyncWorld.wrap(world, player);
    }
}
