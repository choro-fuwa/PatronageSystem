<template>
  <div class="fund-profile">
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="基本信息" name="basicInfo">
        <el-card>
          <el-row :gutter="20">
            <el-col :span="8"><div>基金代码：{{fundProfile.basicInfo.fundCode}}</div></el-col>
            <el-col :span="8"><div>基金名称：{{fundProfile.basicInfo.fundName}}</div></el-col>
            <el-col :span="8"><div>成立日期：{{fundProfile.basicInfo.establishDate}}</div></el-col>
            <el-col :span="8"><div>基金类型：{{fundProfile.basicInfo.fundType}}</div></el-col>
            <el-col :span="8"><div>基金公司：{{fundProfile.basicInfo.companyName}}</div></el-col>
            <el-col :span="8"><div>基金经理：{{fundProfile.basicInfo.managerName}}</div></el-col>
          </el-row>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="业绩表现" name="performance">
        <el-card>
          <div class="performance-chart">
            <!-- 业绩图表 -->
          </div>
          <el-table :data="fundProfile.performance" stripe>
            <el-table-column prop="timePeriod" label="时间周期"></el-table-column>
            <el-table-column prop="returnRate" label="收益率"></el-table-column>
            <el-table-column prop="benchmarkReturn" label="基准收益率"></el-table-column>
            <el-table-column prop="rankPercent" label="同类排名百分位"></el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="持仓分析" name="positions">
        <el-card>
          <el-table :data="fundProfile.positions" stripe>
            <el-table-column prop="stockCode" label="股票代码"></el-table-column>
            <el-table-column prop="stockName" label="股票名称"></el-table-column>
            <el-table-column prop="positionPercent" label="持仓比例"></el-table-column>
            <el-table-column prop="industry" label="所属行业"></el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="业绩归因" name="attribution">
        <el-card>
          <div class="attribution-chart">
            <!-- 归因图表 -->
          </div>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="基金公告" name="announcements">
        <el-card>
          <el-table :data="fundProfile.announcements" stripe>
            <el-table-column prop="announcementDate" label="公告日期"></el-table-column>
            <el-table-column prop="title" label="公告标题"></el-table-column>
            <el-table-column label="操作">
              <template v-slot="scope">
                <el-button size="mini" @click="viewAnnouncement(scope.row.url)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'basicInfo',
      fundProfile: {}
    }
  },
  methods: {
    async fetchFundProfile() {
      try {
        const response = await this.$axios.get(`/api/fund/${this.$route.params.fundCode}/profile`);
        this.fundProfile = response.data;
      } catch (error) {
        this.$message.error('获取基金详情失败');
      }
    },
    viewAnnouncement(url) {
      window.open(url, '_blank');
    }
  },
  async created() {
    await this.fetchFundProfile();
  }
}
</script>    