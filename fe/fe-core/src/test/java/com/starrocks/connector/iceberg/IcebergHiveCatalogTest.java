// Copyright 2021-present StarRocks, Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


package com.starrocks.connector.iceberg;

import com.google.common.collect.ImmutableList;
import com.starrocks.connector.iceberg.hive.IcebergHiveCatalog;
import mockit.Expectations;
import mockit.Mocked;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.hive.HiveCatalog;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IcebergHiveCatalogTest {

    @Test
    public void testListAllDatabases(@Mocked HiveCatalog hiveCatalog) {
        new Expectations() {
            {
                hiveCatalog.listNamespaces();
                result = ImmutableList.of(Namespace.of("db1"), Namespace.of("db2"));
                times = 1;
            }
        };

        Map<String, String> icebergProperties = new HashMap<>();
        icebergProperties.put("hive.metastore.uris", "thrift://129.1.2.3:9876");
        IcebergHiveCatalog icebergHiveCatalog = new IcebergHiveCatalog(
                "hive_native_catalog", new Configuration(), icebergProperties);
        List<String> dbs = icebergHiveCatalog.listAllDatabases();
        Assert.assertEquals(Arrays.asList("db1", "db2"), dbs);
    }
}
