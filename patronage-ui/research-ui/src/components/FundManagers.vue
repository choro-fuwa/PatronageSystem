<template>
  <div class="page-container">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="demo-form-inline">
        <el-form-item label="经理姓名">
          <el-input v-model="filterForm.managerName" placeholder="请输入经理姓名"></el-input>
        </el-form-item>
        <el-form-item label="任职年限">
          <el-select v-model="filterForm.workingYears" placeholder="请选择任职年限">
            <el-option v-for="item in workingYearsOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="管理规模">
          <el-select v-model="filterForm.managementScale" placeholder="请选择管理规模">
            <el-option v-for="item in managementScales" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="经理标签">
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
      <el-table :data="managerList" stripe @row-click="handleRowClick"
                highlight-current-row>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="managerName" label="经理姓名"></el-table-column>
        <el-table-column prop="gender" label="性别"></el-table-column>
        <el-table-column prop="companyName" label="所属公司"></el-table-column>
        <el-table-column prop="workingYears" label="任职年限"></el-table-column>
        <el-table-column prop="managementScale" label="管理规模"></el-table-column>
        <el-table-column label="经理标签">
          <template v-slot="scope">
            <el-tag v-for="tag in scope.row.tags" :key="tag" size="mini">{{tag}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最佳回报">
          <template v-slot="scope">
            <span class="text-red">{{scope.row.bestReturn}}</span>
          </template>
        </el-table-column>
        <el-table-column label="管理基金数">
          <template v-slot="scope">
            {{scope.row.managedFundCount}}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button size="mini" type="primary" @click="viewManager(scope.row.managerId)">查看详情</el-button>
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
        managerName: '',
        workingYears: '',
        managementScale: '',
        tags: []
      },
      workingYearsOptions: [
        { value: '1年以下', label: '1年以下' },
        { value: '1-3年', label: '1-3年' },
        { value: '3-5年', label: '3-5年' },
        { value: '5-10年', label: '5-10年' },
        { value: '10年以上', label: '10年以上' }
      ],
      managementScales: [
        { value: '0-50亿', label: '0-50亿' },
        { value: '50-100亿', label: '50-100亿' },
        { value: '100-500亿', label: '100-500亿' },
        { value: '500亿以上', label: '500亿以上' }
      ],
      allTags: [],
      managerList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    async search() {
      try {
        const response = await this.$axios.get('/api/fund/managers', {
          params: {
            ...this.filterForm,
            page: this.currentPage,
            size: this.pageSize
          }
        });
        this.managerList = response.data.records;
        this.total = response.data.total;
      } catch (error) {
        this.$message.error('获取基金经理列表失败');
      }
    },
    reset() {
      this.filterForm = {
        managerName: '',
        workingYears: '',
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
      this.$refs.managerTable.toggleRowSelection(row);
    }
  },
  async created() {
    try {
      const response = await this.$axios.get('/api/fund/manager/tags');
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

.text-red {
  color: #f56c6c;
}
</style>  