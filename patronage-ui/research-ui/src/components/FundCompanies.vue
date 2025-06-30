<template>
  <div class="page-container">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="demo-form-inline">
        <el-form-item label="公司名称">
          <el-input v-model="filterForm.companyName" placeholder="请输入公司名称"></el-input>
        </el-form-item>
        <el-form-item label="公司类型">
          <el-select v-model="filterForm.companyType" placeholder="请选择公司类型">
            <el-option v-for="item in companyTypes" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="管理规模">
          <el-select v-model="filterForm.managementScale" placeholder="请选择管理规模">
            <el-option v-for="item in managementScales" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="公司标签">
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
      <el-table :data="companyList" stripe @row-click="handleRowClick"
                highlight-current-row>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="companyName" label="公司名称"></el-table-column>
        <el-table-column prop="companyType" label="公司类型"></el-table-column>
        <el-table-column prop="establishDate" label="成立日期"></el-table-column>
        <el-table-column prop="registeredCapital" label="注册资本"></el-table-column>
        <el-table-column prop="managementScale" label="管理规模"></el-table-column>
        <el-table-column label="公司标签">
          <template v-slot="scope">
            <el-tag v-for="tag in scope.row.tags" :key="tag" size="mini">{{tag}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="基金数量">
          <template v-slot="scope">
            {{scope.row.fundCount}}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button size="mini" type="primary" @click="viewCompany(scope.row.companyId)">查看详情</el-button>
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
        companyName: '',
        companyType: '',
        managementScale: '',
        tags: []
      },
      companyTypes: [
        { value: '中资', label: '中资' },
        { value: '合资', label: '合资' },
        { value: '外资', label: '外资' }
      ],
      managementScales: [
        { value: '0-500亿', label: '0-500亿' },
        { value: '500-1000亿', label: '500-1000亿' },
        { value: '1000-5000亿', label: '1000-5000亿' },
        { value: '5000亿以上', label: '5000亿以上' }
      ],
      allTags: [],
      companyList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    async search() {
      try {
        const response = await this.$axios.get('/api/fund/companies', {
          params: {
            ...this.filterForm,
            page: this.currentPage,
            size: this.pageSize
          }
        });
        this.companyList = response.data.records;
        this.total = response.data.total;
      } catch (error) {
        this.$message.error('获取基金公司列表失败');
      }
    },
    reset() {
      this.filterForm = {
        companyName: '',
        companyType: '',
        managementScale: '',
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

    handleRowClick(row) {
      this.$refs.companyTable.toggleRowSelection(row);
    }
  },
  async created() {
    try {
      const response = await this.$axios.get('/api/fund/company/tags');
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
</style>  