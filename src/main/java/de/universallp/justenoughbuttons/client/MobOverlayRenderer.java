package de.universallp.justenoughbuttons.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by universal on 28.08.2016 20:24.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENSE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */
public class MobOverlayRenderer {

    private static Map<BlockPos, SpawnType> cache = new HashMap<BlockPos, SpawnType>();

    public static void renderMobSpawnOverlay() {
        GlStateManager.pushMatrix();
        {
            GlStateManager.disableLighting();
            GlStateManager.disableTexture2D();
            GlStateManager.translate(-ClientProxy.renderManager.viewerPosX, -ClientProxy.renderManager.viewerPosY, -ClientProxy.renderManager.viewerPosZ);

            GL11.glLineWidth(1.5F);
            GlStateManager.color(1, 0, 0);

            for (BlockPos p : cache.keySet()) {
                SpawnType t = cache.get(p);

                if (t == SpawnType.ALWAYS)
                    renderCross(p, 1, 0, 0);
                else
                    renderCross(p, 1, 1, 0);
            }

            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
        GlStateManager.popMatrix();
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
        VertexBuffer renderer = tess.getBuffer();

        renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos(x0, y, z0).color(r, g, b, 1).endVertex();
        renderer.pos(x1, y, z1).color(r, g, b, 1).endVertex();

        renderer.pos(x1, y, z0).color(r, g, b, 1).endVertex();
        renderer.pos(x0, y, z1).color(r, g, b, 1).endVertex();
        tess.draw();
    }

    public static void cacheMobSpawns(Entity entity) {
        cache.clear();

        World world = entity.world;
        int entX = (int) entity.posX;
        int entY = MathHelper.clamp((int) entity.posY, 16, world.getHeight() - 16);
        int entZ = (int) entity.posZ;

        for (int x = entX - 16; x <= entX + 16; x++) {
            for (int z = entZ - 16; z <= entZ + 16; z++) {
                BlockPos pos = new BlockPos(x, entY, z);
                Chunk chunk = world.getChunkFromBlockCoords(pos);
                Biome biome = world.getBiome(pos);

                if (biome.getSpawnableList(EnumCreatureType.MONSTER).isEmpty() || biome.getSpawningChance() <= 0)
                    continue;

                for (int y = entY - 16; y < entY + 16; y++) {
                    if (!world.isAirBlock(new BlockPos(x, y, z)))
                        continue;
                    SpawnType spawnType = getSpawnType(chunk, x, y, z);
                    if (spawnType != SpawnType.NEVER)
                        cache.put(new BlockPos(x, y, z), spawnType);
                }
            }
        }
    }

    private static SpawnType getSpawnType(Chunk chunk, int x, int y, int z) {
        World world = chunk.getWorld();
        BlockPos pos = new BlockPos(x, y, z);

        if (!WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, world, pos) || chunk.getLightFor(EnumSkyBlock.BLOCK, pos) >= 8)
            return SpawnType.NEVER;

        BlockPos p = new BlockPos(x, y , z);
        AxisAlignedBB aabb = new AxisAlignedBB(p);

        if (!world.checkNoEntityCollision(aabb) || !world.getEntitiesWithinAABBExcludingEntity(null, aabb).isEmpty() || world.containsAnyLiquid(aabb))
            return SpawnType.NEVER;

        if (chunk.getLightFor(EnumSkyBlock.SKY, pos) >= 8)
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
