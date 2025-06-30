<template>
  <div class="page-container">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="demo-form-inline">
        <el-form-item label="组合名称">
          <el-input v-model="filterForm.combinationName" placeholder="请输入组合名称"></el-input>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
              v-model="filterForm.createTime"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">搜索</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <div class="table-toolbar">
        <el-button type="primary" @click="createCombination">创建组合</el-button>
        <el-button type="danger" @click="batchDelete">批量删除</el-button>
      </div>

      <el-table :data="combinationList" stripe @row-click="handleRowClick"
                highlight-current-row>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="combinationName" label="组合名称"></el-table-column>
        <el-table-column prop="fundCount" label="基金数量"></el-table-column>
        <el-table-column label="创建时间">
          <template v-slot="scope">
            {{scope.row.createTime}}
          </template>
        </el-table-column>
        <el-table-column label="创建者">
          <template v-slot="scope">
            {{scope.row.creator}}
          </template>
        </el-table-column>
        <el-table-column label="平均收益">
          <template v-slot="scope">
            <span :class="{'text-red': scope.row.averageReturn > 0, 'text-green': scope.row.averageReturn < 0}">
              {{scope.row.averageReturn > 0 ? '+' : ''}}{{scope.row.averageReturn}}%
            </span>
          </template>
        </el-table-column>
        <el-table-column label="波动率">
          <template v-slot="scope">
            {{scope.row.volatility}}%
          </template>
        </el-table-column>
        <el-table-column label="夏普比率">
          <template v-slot="scope">
            {{scope.row.sharpeRatio}}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button size="mini" type="primary" @click="viewCombination(scope.row.id)">查看详情</el-button>
            <el-button size="mini" type="warning" @click="editCombination(scope.row.id)">编辑</el-button>
            <el-button size="mini" type="danger" @click="deleteCombination(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      filterForm: {
        combinationName: '',
        createTime: []
      },
      combinationList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    async search() {
      try {
        const response = await this.$axios.get('/api/fund/combinations', {
          params: {
            ...this.filterForm,
            page: this.currentPage,
            size: this.pageSize
          }
        });
        this.combinationList = response.data.records;
        this.total = response.data.total;
      } catch (error) {
        this.$message.error('获取基金组合列表失败');
      }
    },
    reset() {
      this.filterForm = {
        combinationName: '',
        createTime: []
      };
      this.currentPage = 1;
      this.search();
    },
    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.search();
    },
    handleCurrentChange(newPage) {
      this.currentPage = newPage;
      this.search();
    },
    viewCombination(id) {
      this.$router.push({ name: 'CombinationDetail', params: { id } });
    },
    createCombination() {
      // 创建组合逻辑
    },


    batchDelete() {
      // 批量删除逻辑
    },
    handleRowClick(row) {
      this.$refs.combinationTable.toggleRowSelection(row);
    }
  },
  async created() {
    this.search();
  }
}
</script>

<style scoped>
.page-container {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.table-toolbar {
  margin-bottom: 15px;
}

.text-red {
  color: #f56c6c;
}

.text-green {
  color: #67c23a;
}
</style>  