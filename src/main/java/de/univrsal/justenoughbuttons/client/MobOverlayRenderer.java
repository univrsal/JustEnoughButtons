package de.univrsal.justenoughbuttons.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.lighting.IWorldLightListener;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by universal on 28.08.2016 20:24.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENSE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */
public class MobOverlayRenderer {

    private static Map<BlockPos, SpawnType> cache = new HashMap<BlockPos, SpawnType>();

    public static void renderMobSpawnOverlay() {
        ActiveRenderInfo ai = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();

        RenderSystem.pushMatrix();
        {
            RenderSystem.disableBlend();
            RenderSystem.disableLighting();
            RenderSystem.disableTexture();

            RenderSystem.translated(-ai.getProjectedView().x, -(ai.getProjectedView().y),
                    -ai.getProjectedView().z);

            GL11.glLineWidth(1.5F);
            RenderSystem.color3f(1, 0, 0);

            for (BlockPos p : cache.keySet()) {
                SpawnType t = cache.get(p);

                if (t == SpawnType.ALWAYS)
                    renderCross(p, 1, 0, 0);
                else
                    renderCross(p, 1, 1, 0);
            }

            RenderSystem.enableLighting();
            RenderSystem.enableTexture();
            RenderSystem.enableBlend();
        }
        RenderSystem.popMatrix();
    }

    /**
     * Blatantly copied
     * @author feldim2425
     * @param pos
     * @param r
     * @param g
     * @param b
     */
    private static void renderCross(BlockPos pos, float r, float g, float b) {
        double y = pos.getY() + 0.005D;

        double x0 = pos.getX();
        double x1 = x0 + 1;
        double z0 = pos.getZ();
        double z1 = z0 + 1;

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder renderer = tess.getBuffer();

        renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        renderer.func_225582_a_(x0, y, z0).func_227885_a_(r, g, b, 1).endVertex();
        renderer.func_225582_a_(x1, y, z1).func_227885_a_(r, g, b, 1).endVertex();

        renderer.func_225582_a_(x1, y, z0).func_227885_a_(r, g, b, 1).endVertex();
        renderer.func_225582_a_(x0, y, z1).func_227885_a_(r, g, b, 1).endVertex();
        tess.draw();
    }

    public static void cacheMobSpawns(Entity entity) {
        cache.clear();

        World world = entity.world;

        int entX = (int) entity.serverPosX;
        int entY = MathHelper.clamp((int) entity.serverPosY, 16, world.getHeight() - 16);
        int entZ = (int) entity.serverPosZ;

        for (int x = entX - 16; x <= entX + 16; x++) {
            for (int z = entZ - 16; z <= entZ + 16; z++) {
                BlockPos pos = new BlockPos(x, entY, z);
                IChunk chunk = world.getChunk(pos);
                Biome biome = world.func_226691_t_(pos);

                if (biome.getSpawns(EntityClassification.CREATURE.MONSTER).isEmpty() || biome.getSpawningChance() <= 0)
                    continue;

                for (int y = entY - 16; y < entY + 16; y++) {
                    if (!world.isAirBlock(new BlockPos(x, y, z)))
                        continue;
                    SpawnType spawnType = getSpawnType(world, chunk, x, y, z);
                    if (spawnType != SpawnType.NEVER)
                        cache.put(new BlockPos(x, y, z), spawnType);
                }
            }
        }
    }

    private static SpawnType getSpawnType(IWorldReader w, IChunk chunk, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState bs = w.getBlockState(pos);
        IFluidState fs = w.getFluidState(pos);
        IWorldLightListener ble = Objects.requireNonNull(w.func_225524_e_()).getLightEngine(LightType.BLOCK);
        int block_light = ble.getLightFor(pos);

        World world = (World) w;
        if (!EntitySpawnPlacementRegistry.PlacementType.ON_GROUND.canSpawnAt(w, pos, null)
                || block_light >= 8) {
            return SpawnType.NEVER;
        }

        AxisAlignedBB aabb = new AxisAlignedBB(pos);
        VoxelShape vs = VoxelShapes.create(aabb);

        if (!w.checkNoEntityCollision(null, vs) || /* Collision checks */
                !world.getEntitiesWithinAABBExcludingEntity(null, aabb).isEmpty()
                || world.containsAnyLiquid(aabb)) {
            return SpawnType.NEVER;
        }

        if (w.canBlockSeeSky(pos))
            return SpawnType.NIGHT_ONLY;
        return SpawnType.ALWAYS;
    }

    public static void clearCache() {
        cache.clear();
    }

    private enum SpawnType {
        NEVER,
        NIGHT_ONLY,
        ALWAYS
    }
}
