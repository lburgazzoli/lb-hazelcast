/*
 * Copyright (c) 2015 Luca Burgazzoli
 *
 * https://github.com/lburgazzoli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lburgazzoli.hazelcast.config.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.github.lburgazzoli.hazelcast.config.HzHierarchicalMapConfigBuilder;
import com.hazelcast.config.Config;

import java.io.InputStream;
import java.util.Map;

public class JsonConfigBuilder extends HzHierarchicalMapConfigBuilder {

    private InputStream in;

    public JsonConfigBuilder(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream can't be null");
        }

        this.in = inputStream;
    }

    @Override
    public Config build() {
        return build(new Config());
    }

    @SuppressWarnings("unchecked")
    Config build(Config config) {
        try {
            return process(
                config,
                new ObjectMapper()
                    .registerModule(new AfterburnerModule())
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .setPropertyNamingStrategy(new LowerCaseWithDashStrategy())
                    .readValue(this.in, Map.class)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // *************************************************************************
    //
    // *************************************************************************

    private static class LowerCaseWithDashStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {
        @Override
        public String translate(String input) {
            if(input == null) {
                return input;
            } else {
                int length = input.length();
                int resultLength = 0;
                boolean wasPrevTranslated = false;

                final StringBuilder result = new StringBuilder(length * 2);

                for(int i = 0; i < length; ++i) {
                    char c = input.charAt(i);
                    if(i > 0 || c != 95) {
                        if(Character.isUpperCase(c)) {
                            if(!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != 95) {
                                result.append('-');
                                ++resultLength;
                            }

                            c = Character.toLowerCase(c);
                            wasPrevTranslated = true;
                        } else {
                            wasPrevTranslated = false;
                        }

                        result.append(c);
                        ++resultLength;
                    }
                }

                return resultLength > 0 ? result.toString() : input;
            }
        }
    }
}
