<template>
  <div class="fund-search">
    <div class="search-options">
      <el-input v-model="fundCode" placeholder="请输入基金代码"></el-input>
      <el-select v-model="selectedTags" multiple placeholder="请选择标签">
        <el-option v-for="tag in allTags" :key="tag" :label="tag" :value="tag"></el-option>
      </el-select>
      <el-button @click="searchFunds">搜索</el-button>
    </div>

    <el-table :data="fundList" stripe>
      <el-table-column prop="fundCode" label="基金代码"></el-table-column>
      <el-table-column prop="fundName" label="基金名称"></el-table-column>
      <el-table-column prop="companyName" label="基金公司"></el-table-column>
      <el-table-column prop="managerName" label="基金经理"></el-table-column>
      <el-table-column label="标签">
        <template v-slot="scope">
          <el-tag v-for="tag in scope.row.tags" :key="tag" size="mini">{{tag}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button size="mini" @click="viewProfile(scope.row.fundCode)">查看详情</el-button>
          <el-checkbox v-model="selectedFunds" :label="scope.row.fundCode"></el-checkbox>
        </template>
      </el-table-column>
    </el-table>

    <el-button @click="savePortfolio" :disabled="selectedFunds.length === 0">保存为组合</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      fundCode: '',
      selectedTags: [],
      allTags: [],
      fundList: [],
      selectedFunds: []
    }
  },
  methods: {
    async searchFunds() {
      try {
        const response = await this.$axios.get('/api/fund/all', {
          params: {
            fundCode: this.fundCode,
            tags: this.selectedTags
          }
        });
        this.fundList = response.data;
      } catch (error) {
        this.$message.error('获取基金数据失败');
      }
    },
    async viewProfile(fundCode) {
      this.$router.push({ name: 'FundProfile', params: { fundCode } });
    },
    async savePortfolio() {
      try {
        const response = await this.$axios.post('/api/fund/portfolio', this.selectedFunds);
        this.$message.success(`组合保存成功，ID：${response.data}`);
      } catch (error) {
        this.$message.error('保存组合失败');
      }
    }
  },
  async created() {
    try {
      const response = await this.$axios.get('/api/fund/tags');
      this.allTags = response.data;
    } catch (error) {
      this.$message.error('获取标签数据失败');
    }
  }
}
</script>    