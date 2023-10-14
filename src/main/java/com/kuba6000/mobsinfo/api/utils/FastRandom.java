/*
 * spotless:off
 * MobsInfo - Minecraft addon
 * Copyright (C) 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package com.kuba6000.mobsinfo.api.utils;

import java.util.Random;
import java.util.SplittableRandom;

public class FastRandom extends Random {

    private static final long serialVersionUID = -6330702242734689581L;
    private SplittableRandom realRandom;

    public FastRandom() {
        realRandom = new SplittableRandom();
    }

    public FastRandom(long seed) {
        realRandom = new SplittableRandom(seed);
    }

    @Override
    public synchronized void setSeed(long seed) {
        realRandom = new SplittableRandom(seed);
    }

    @Override
    protected int next(int bits) {
        return (realRandom.nextInt() >>> (32 - bits));
    }
}
