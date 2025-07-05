<template>
  <div class="page-container">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="demo-form-inline">
        <el-form-item label="基金代码">
          <el-input v-model="filterForm.fundCode" placeholder="请输入基金代码"></el-input>
        </el-form-item>
        <el-form-item label="基金名称">
          <el-input v-model="filterForm.fundName" placeholder="请输入基金名称"></el-input>
        </el-form-item>
        <el-form-item label="基金类型">
          <el-select v-model="filterForm.fundType" placeholder="请选择基金类型">
            <el-option v-for="item in fundTypes" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="基金标签">
          <el-select v-model="filterForm.tags" multiple placeholder="请选择标签">
            <el-option v-for="tag in allTags" :key="tag" :label="tag" :value="tag"></el-option>
          </el-select>
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
        <el-button type="success" @click="batchAddToCombination">批量加入组合</el-button>
        <el-button type="danger" @click="batchDelete">批量删除</el-button>
      </div>

      <el-table :data="fundList" stripe @row-click="handleRowClick" ref="fundTable"
                highlight-current-row>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="fundCode" label="基金代码"></el-table-column>
        <el-table-column prop="fundName" label="基金名称"></el-table-column>
        <el-table-column prop="fundType" label="基金类型"></el-table-column>
        <el-table-column prop="companyName" label="基金公司"></el-table-column>
        <el-table-column prop="managerName" label="基金经理"></el-table-column>
        <el-table-column label="基金标签">
          <template v-slot="scope">
            <el-tag v-for="tag in scope.row.tags" :key="tag" size="mini">{{tag}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最新净值">
          <template v-slot="scope">
            <span :class="{'text-red': scope.row.netValueGrowth > 0, 'text-green': scope.row.netValueGrowth < 0}">
              {{scope.row.latestNetValue}}
            </span>
            <span class="ml-2" :class="{'text-red': scope.row.netValueGrowth > 0, 'text-green': scope.row.netValueGrowth < 0}">
              {{scope.row.netValueGrowth > 0 ? '+' : ''}}{{scope.row.netValueGrowth}}%
            </span>
          </template>
        </el-table-column>
        <el-table-column label="成立日期">
          <template v-slot="scope">
            {{scope.row.establishDate}}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button size="mini" type="primary" @click="viewFund(scope.row.fundCode)">查看详情</el-button>
            <el-button size="mini" type="success" @click="addToCombination(scope.row.fundCode)">加入组合</el-button>
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
        fundCode: '',
        fundName: '',
        fundType: '',
        tags: []
      },
      fundTypes: [
        { value: '混合型', label: '混合型' },
        { value: '股票型', label: '股票型' },
        { value: '债券型', label: '债券型' },
        { value: '货币型', label: '货币型' },
        { value: '指数型', label: '指数型' },
        { value: 'QDII', label: 'QDII' }
      ],
      allTags: [],
      fundList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    async search() {
      try {
        const response = await this.$axios.get('/api/fund/all', {
          params: {
            ...this.filterForm,
            page: this.currentPage,
            size: this.pageSize
          }
        });
        this.fundList = response.data.records;
        this.total = response.data.total;
      } catch (error) {
        this.$message.error('获取基金列表失败');
      }
    },
    reset() {
      this.filterForm = {
        fundCode: '',
        fundName: '',
        fundType: '',
        tags: []
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
    viewFund(fundCode) {
      this.$router.push({ name: 'FundProfile', params: { fundCode } });
    },

    createCombination() {
      // 创建组合逻辑
    },
    batchAddToCombination() {
      // 批量加入组合逻辑
    },
    batchDelete() {
      // 批量删除逻辑
    },
    handleRowClick(row) {
      this.$refs.fundTable.toggleRowSelection(row);
    }
  },
  async created() {
    try {
      const response = await this.$axios.get('/api/fund/tags');
      this.allTags = response.data;
      this.search();
    } catch (error) {
      this.$message.error('获取标签数据失败');
    }
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

.ml-2 {
  margin-left: 8px;
}
</style>  