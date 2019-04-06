package com.github.highd120.block.injection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.util.HasNbtTagData;
import com.github.highd120.util.MathUtil;
import com.github.highd120.util.NbtTagUtil;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class InjectionEffectManager implements HasNbtTagData {

    enum State {
        WAIT(-1), WISP_UP(20), WISP_WAIT(20), WISP_AGGREGATE(20), WISP_COMPOSE(40);
        private int time;

        private State(int time) {
            this.time = time;
        }

        public int getTime() {
            return time;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    class WispData implements HasNbtTagData {
        private Vec3d position;
        private Vec3d speed;

        /**
         * 更新。
         */
        public void upDate() {
            position = position.add(speed);
        }

        @Override
        public void subReadNbt(NBTTagCompound tag) {
            position = NbtTagUtil.readVec3d(tag, "position");
            speed = NbtTagUtil.readVec3d(tag, "speed");
        }

        @Override
        public void subWriteNbt(NBTTagCompound tag) {
            NbtTagUtil.writeVec3d(tag, "position", position);
            NbtTagUtil.writeVec3d(tag, "speed", speed);
        }

        @Override
        public String getName() {
            return "wisp";
        }
    }

    private static State[] stateList = State.values();

    private State state = State.WAIT;
    private int time = 0;
    private List<WispData> wispList;
    private Vec3d center;

    public InjectionEffectManager() {
        wispList = new ArrayList<>();
        center = new Vec3d(0, 0, 0);
    }

    /**
     * コンストラクター。
     * @param pointList 座標のリスト。
     * @param center 中心座標。
     */
    public InjectionEffectManager(List<BlockPos> pointList, BlockPos center) {
        this.wispList = pointList.stream().map(point -> {
            Vec3d postion = MathUtil.blockPosToVec3dCenter(point).addVector(0, 0.8, 0);
            return new WispData(postion, new Vec3d(0, 0, 0));
        }).collect(Collectors.toList());
        this.center = MathUtil.blockPosToVec3dCenter(center);
        wispList.add(new WispData(this.center.addVector(0.0, 0.5, 0.0), new Vec3d(0, 0, 0)));
    }

    /**
     * 更新。
     */
    public void upDate() {
        time++;
        if (time == state.getTime()) {
            if (state.ordinal() + 1 == stateList.length) {
                return;
            }
            state = stateList[state.ordinal() + 1];
            wispList.forEach(data -> changeWispData(data));
            time = 0;
        }
        wispList.forEach(data -> data.upDate());
    }

    /**
     * ウェイスプを出す。
     */
    public void spawnWisp() {
        wispList.forEach(data -> BotaniaExMain.proxy.spawnWispInjection(data.position));
    }

    /**
     * エフェクトの動作開始。
     */
    public void start() {
        state = State.WISP_UP;
        wispList.forEach(data -> changeWispData(data));
        time = 0;
    }

    public boolean isEnd() {
        return time >= state.getTime() && state == State.WISP_COMPOSE;
    }

    void changeWispData(WispData data) {
        switch (state) {
        case WISP_AGGREGATE:
            double xSpeed = (center.xCoord - data.position.xCoord) / state.time;
            double zSpeed = (center.zCoord - data.position.zCoord) / state.time;
            data.speed = new Vec3d(xSpeed, 0, zSpeed);
            break;
        case WISP_UP:
            data.speed = new Vec3d(0, 0.2f, 0);
            break;
        case WISP_WAIT:
            data.speed = new Vec3d(0, 0, 0);
            break;
        case WISP_COMPOSE:
            data.speed = new Vec3d(0, 0, 0);
            break;
        default:
            break;
        }
    }

    @Override
    public void subReadNbt(NBTTagCompound tag) {
        time = tag.getInteger("time");
        state = stateList[tag.getInteger("state")];
        center = NbtTagUtil.readVec3d(tag, "center");
        wispList = NbtTagUtil.readList(tag, "list", () -> new WispData());
    }

    @Override
    public void subWriteNbt(NBTTagCompound tag) {
        tag.setInteger("time", time);
        tag.setInteger("state", state.ordinal());
        NbtTagUtil.writeVec3d(tag, "center", center);
        NbtTagUtil.writeList(tag, "list", wispList);
    }

    @Override
    public String getName() {
        return "effect";
    }
}
