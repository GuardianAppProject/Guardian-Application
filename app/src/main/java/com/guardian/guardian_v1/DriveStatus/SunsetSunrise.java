package com.guardian.guardian_v1.DriveStatus;

/*
 * Copyright 2008-2009 Mike Reedell / LuckyCatLabs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.math.BigDecimal;

/**
 * Defines the solar declination used in computing the sunrise/sunset.
 */
public class SunsetSunrise {
    /** Astronomical sunrise/set is when the sun is 18 degrees below the horizon. */
    public static final SunsetSunrise ASTRONOMICAL = new SunsetSunrise(108);

    /** Nautical sunrise/set is when the sun is 12 degrees below the horizon. */
    public static final SunsetSunrise NAUTICAL = new SunsetSunrise(102);

    /** Civil sunrise/set (dawn/dusk) is when the sun is 6 degrees below the horizon. */
    public static final SunsetSunrise CIVIL = new SunsetSunrise(96);

    /** Official sunrise/set is when the sun is 50' below the horizon. */
    public static final SunsetSunrise OFFICIAL = new SunsetSunrise(90.8333);

    private final BigDecimal degrees;

    public SunsetSunrise(double degrees) {
        this.degrees = BigDecimal.valueOf(degrees);
    }

    public BigDecimal degrees() {
        return degrees;
    }
}
