<template>
  <div class="factor-category">
    <div class="page-header">
      <el-button @click="$router.back()" icon="ArrowLeft">返回</el-button>
      <h2>因子分类管理</h2>
      <el-button type="primary" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        新增分类
      </el-button>
    </div>

    <!-- 分类列表 -->
    <div class="category-content">
      <el-row :gutter="20">
        <!-- 分类树 -->
        <el-col :span="8">
          <el-card>
            <template #header>
              <span>分类结构</span>
            </template>
            <el-tree
              :data="categoryTree"
              :props="treeProps"
              node-key="id"
              :default-expand-all="true"
              @node-click="handleNodeClick"
            >
              <template #default="{ node, data }">
                <div class="tree-node">
                  <span>{{ node.label }}</span>
                  <div class="node-actions">
                    <el-button size="small" @click.stop="editCategory(data)">编辑</el-button>
                    <el-button size="small" type="danger" @click.stop="deleteCategory(data.id)">删除</el-button>
                  </div>
                </div>
              </template>
            </el-tree>
          </el-card>
        </el-col>

        <!-- 分类详情 -->
        <el-col :span="16">
          <el-card v-if="selectedCategory">
            <template #header>
              <span>分类详情</span>
            </template>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="分类名称">{{ selectedCategory.categoryName }}</el-descriptions-item>
              <el-descriptions-item label="分类代码">{{ selectedCategory.categoryCode }}</el-descriptions-item>
              <el-descriptions-item label="分类描述">{{ selectedCategory.description || '-' }}</el-descriptions-item>
              <el-descriptions-item label="排序">{{ selectedCategory.sortOrder }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="selectedCategory.status === 1 ? 'success' : 'danger'">
                  {{ selectedCategory.status === 1 ? '正常' : '禁用' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ selectedCategory.createTime }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
          
          <el-card v-else>
            <template #header>
              <span>分类详情</span>
            </template>
            <div class="empty-content">
              <el-empty description="请选择一个分类查看详情" />
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 新增/编辑分类对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="editingCategory ? '编辑分类' : '新增分类'"
      width="500px"
    >
      <el-form :model="categoryForm" :rules="categoryRules" ref="categoryFormRef" label-width="100px">
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="categoryForm.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类代码" prop="categoryCode">
          <el-input v-model="categoryForm.categoryCode" placeholder="请输入分类代码" />
        </el-form-item>
        <el-form-item label="父分类" prop="parentId">
          <el-select v-model="categoryForm.parentId" placeholder="请选择父分类" style="width: 100%">
            <el-option label="顶级分类" :value="0" />
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.categoryName"
              :value="category.id"
              :disabled="editingCategory && category.id === editingCategory.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="categoryForm.sortOrder" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="categoryForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分类描述" prop="description">
          <el-input
            v-model="categoryForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入分类描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveCategory">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, Plus } from '@element-plus/icons-vue'
import { factorApi } from '@/api/factor'

// 数据
const categories = ref([])
const categoryTree = ref([])
const selectedCategory = ref(null)
const showAddDialog = ref(false)
const editingCategory = ref(null)
const categoryFormRef = ref()

// 树形配置
const treeProps = {
  children: 'children',
  label: 'categoryName'
}

// 分类表单
const categoryForm = reactive({
  categoryName: '',
  categoryCode: '',
  parentId: 0,
  sortOrder: 0,
  status: 1,
  description: ''
})

// 表单验证规则
const categoryRules = {
  categoryName: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  categoryCode: [{ required: true, message: '请输入分类代码', trigger: 'blur' }],
  parentId: [{ required: true, message: '请选择父分类', trigger: 'change' }]
}

// 获取分类列表
const getCategories = async () => {
  try {
    const response = await factorApi.getCategories()
    if (response.success) {
      categories.value = response.data
      buildCategoryTree()
    }
  } catch (error) {
    ElMessage.error('获取分类列表失败')
  }
}

// 构建分类树
const buildCategoryTree = () => {
  const categoryMap = new Map()
  const tree = []
  
  // 创建映射
  categories.value.forEach(category => {
    categoryMap.set(category.id, { ...category, children: [] })
  })
  
  // 构建树结构
  categories.value.forEach(category => {
    const node = categoryMap.get(category.id)
    if (category.parentId === 0 || !categoryMap.has(category.parentId)) {
      tree.push(node)
    } else {
      const parent = categoryMap.get(category.parentId)
      parent.children.push(node)
    }
  })
  
  categoryTree.value = tree
}

// 节点点击处理
const handleNodeClick = (data) => {
  selectedCategory.value = data
}

// 编辑分类
const editCategory = (category) => {
  editingCategory.value = category
  Object.assign(categoryForm, category)
  showAddDialog.value = true
}

// 删除分类
const deleteCategory = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个分类吗？删除后无法恢复。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await factorApi.deleteCategory(id)
    if (response.success) {
      ElMessage.success('删除成功')
      getCategories()
      if (selectedCategory.value && selectedCategory.value.id === id) {
        selectedCategory.value = null
      }
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 保存分类
const saveCategory = async () => {
  try {
    await categoryFormRef.value.validate()
    
    const api = editingCategory.value ? factorApi.updateCategory : factorApi.addCategory
    const response = await api(categoryForm)
    
    if (response.success) {
      ElMessage.success(editingCategory.value ? '更新成功' : '新增成功')
      showAddDialog.value = false
      resetForm()
      getCategories()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

// 重置表单
const resetForm = () => {
  editingCategory.value = null
  Object.assign(categoryForm, {
    categoryName: '',
    categoryCode: '',
    parentId: 0,
    sortOrder: 0,
    status: 1,
    description: ''
  })
}

// 初始化
onMounted(() => {
  getCategories()
})
</script>

<style scoped>
.factor-category {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.category-content {
  margin-top: 20px;
}

.tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.node-actions {
  display: flex;
  gap: 5px;
  opacity: 0;
  transition: opacity 0.3s;
}

.tree-node:hover .node-actions {
  opacity: 1;
}

.empty-content {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}

:deep(.el-tree-node__content) {
  height: auto;
  padding: 8px 0;
}

:deep(.el-tree-node__label) {
  flex: 1;
}
</style> 