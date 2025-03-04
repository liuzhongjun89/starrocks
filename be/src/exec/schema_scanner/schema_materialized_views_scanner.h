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

#pragma once

#include "column/datum.h"
#include "exec/schema_scanner.h"
#include "gen_cpp/FrontendService_types.h"
#include "runtime/global_dict/parser.h"

namespace starrocks {

class SchemaMaterializedViewsScanner : public SchemaScanner {
public:
    SchemaMaterializedViewsScanner();
    ~SchemaMaterializedViewsScanner() override;

    Status start(RuntimeState* state) override;
    Status get_next(ChunkPtr* chunk, bool* eos) override;

private:
    Status get_materialized_views();
    Status _fill_chunk_v1(ChunkPtr* chunk);
    Status _fill_chunk_v2(ChunkPtr* chunk);
    Status _change_to_v1_schema(RuntimeState* state);

    int _db_index{0};
    int _table_index{0};
    TGetDbsResult _db_result;

    // v2 result
    TListMaterializedViewStatusResult _mv_results;
    static SchemaScanner::ColumnDesc _s_tbls_columns[];

    // NOTE v1 result. it's kept for compatibility, and used for 2.5 version
    static SchemaScanner::ColumnDesc _s_tbls_columns_v1[];
    TListTableStatusResult _table_result;
    bool _v1_schema = false;
};

} // namespace starrocks
