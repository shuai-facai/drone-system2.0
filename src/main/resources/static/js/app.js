/*前端交互脚本*/
/**
 * 无人机信息管理系统 - 前端主逻辑
 * 使用 Vue 3 Composition API 开发
 */
const { createApp, ref, reactive } = Vue//从 Vue 3 核心库中引入 createApp、ref、reactive 函数
/*createApp：创建 Vue 应用实例；
ref：创建基本类型的响应式数据（如字符串、数字、布尔值）；
reactive：创建复杂类型的响应式数据（如对象、数组）。*/

/**
 * 创建 Vue 应用实例
 */
const app = createApp({
  setup() {//组合式 API 的入口，所有响应式数据、方法都在这里定义
    // ==================== 状态定义 ====================
    const currentPage = ref('login') // 当前页面：login/list
    const user = reactive({ username: '', password: '' }) // 用户登录信息
    const drones = ref([]) // 无人机列表数据
    const searchKeyword = ref('') // 搜索关键词
    const searchType = ref('model') // 搜索类型：model/uavCode

    // 模态框显示控制(添加/编辑/AI生成)
    const showAddModal = ref(false)
    const showEditModal = ref(false)
    const showAIGenModal = ref(false)

    // 当前编辑的无人机信息
    const currentDrone = reactive({
      id: null, uavCode: '', model: '', manufacturer: '',
      maxPayload: '', maxAltitude: '', maxFlightTime: '',
      maxSpeed: '', wingspan: '', weight: '', status: 1, remark: '', aiGenerated: 0
    })

    const aiCount = ref(1) // AI生成数量

    // ==================== Axios 配置 ====================
    // 创建 axios 实例，配置基础 URL
    const axiosInstance = axios.create({
      baseURL: '/api',
      withCredentials: true
    })

    // 响应拦截器：处理 401 未认证情况
    axiosInstance.interceptors.response.use(
      response => response,// 成功响应直接返回
      error => {
        if (error.response && error.response.status === 401) {
          currentPage.value = 'login'// 跳回登录页
        }
        return Promise.reject(error)
      }
    )

    // ==================== 用户认证相关 ====================
    /**
     * 用户登录
     */
    const handleLogin = async () => {
      try {
        const response = await axiosInstance.post('/login', user)
        if (response.data.success) {
          currentPage.value = 'list'// 跳转列表页
          loadDrones()// 加载无人机列表
        } else {
          alert('登录失败：' + response.data.message)
        }
      } catch (error) {
        alert('登录失败：' + error.message)
      }
    }

    /**
     * 用户退出
     */
    const handleLogout = async () => {
      try {
        await axiosInstance.post('/logout')
      } catch (e) { }
      currentPage.value = 'login'
    }

    // ==================== 无人机数据操作 ====================
    /**
     * 加载无人机列表
     */
    const loadDrones = async () => {
      try {
        const response = await axiosInstance.get('/drones', {
          params: { keyword: searchKeyword.value, type: searchType.value }
        })
        drones.value = response.data.data// 把后端返回的数据赋值给响应式数组
      } catch (error) {
        console.error('Load error:', error)
      }
    }

    /**
     * 搜索无人机
     */
    const handleSearch = () => loadDrones()

    /**
     * 打开添加模态框
     */
    const openAddModal = () => {
      showAddModal.value = true
      resetDroneForm()// 重置无人机表单
    }

    /**
     * 打开编辑模态框
     * @param drone 无人机数据
     */
    const openEditModal = (drone) => {
      showEditModal.value = true
      Object.assign(currentDrone, drone)
    }

    /**
     * 打开AI生成模态框
     */
    const openAIGenModal = () => {
      showAIGenModal.value = true
      aiCount.value = 1
    }

    /**
     * 关闭模态框
     */
    const closeModal = () => {
      showAddModal.value = false
      showEditModal.value = false
    }

    /**
     * 重置无人机表单
     */
    const resetDroneForm = () => {
      Object.assign(currentDrone, {
        id: null, uavCode: '', model: '', manufacturer: '',
        maxPayload: '', maxAltitude: '', maxFlightTime: '',
        maxSpeed: '', wingspan: '', weight: '', status: 1, remark: '', aiGenerated: 0
      })
    }

    /**
     * 保存无人机信息（新增或更新）
     */
    const saveDrone = async () => {
      try {
        if (currentDrone.id) {// 有 id 则更新：PUT /api/drones/{id}
          await axiosInstance.put('/drones/' + currentDrone.id, currentDrone)
        } else { // 无 id 则新增：POST /api/drones
          await axiosInstance.post('/drones', currentDrone)
        }
        alert('保存成功')
        closeModal()// 关闭模态框
        loadDrones()// 加载无人机列表
      } catch (error) {
        alert('保存失败：' + error.message)
      }
    }

    /**
     * 删除无人机
     * @param id 无人机ID
     */
    const deleteDrone = async (id) => {
      if (!confirm('确定删除？')) return
      try {
        await axiosInstance.delete('/drones/' + id)// DELETE /api/drones/{id}
        alert('删除成功')
        loadDrones()
      } catch (error) {
        alert('删除失败：' + error.message)
      }
    }

    /**
     * AI生成无人机数据
     */
    const generateAI = async () => {
      try {
        const response = await axiosInstance.post('/drones/ai/generate', { count: aiCount.value })
        alert(`成功生成 ${response.data.data.length} 条数据`)
        showAIGenModal.value = false
        loadDrones()
      } catch (error) {
        alert('生成失败：' + error.message)
      }
    }

    // ==================== 暴露给模板使用 ====================
    return {
      currentPage, user, drones, searchKeyword, searchType,
      showAddModal, showEditModal, showAIGenModal, currentDrone, aiCount,
      handleLogin, handleLogout, loadDrones, handleSearch,
      openAddModal, openEditModal, openAIGenModal, closeModal,
      saveDrone, deleteDrone, generateAI
    }
  },
  //--v-model,实现表单与响应式数据的双向绑定-->
  //v-if/v-else：控制登录页 / 列表页的显示；
  //v-for：遍历无人机列表渲染表格行；
  template: `
    <!-- 登录页面 -->
    <div v-if="currentPage === 'login'" class="login-container">
      <h2 class="text-center mb-4">无人机信息管理系统</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group"><label>用户名</label><input v-model="user.username" type="text" class="form-control" required></div>
        <div class="form-group"><label>密码</label><input v-model="user.password" type="password" class="form-control" required></div>
        <button type="submit" class="btn btn-primary btn-block">登录</button>
        <p class="text-center text-muted mt-3">默认账号：admin/123 或 user/user</p>
      </form>
    </div>

    <!-- 主页面 -->
    <div v-else class="main-container">
      <div class="sidebar">
        <h3>无人机管理系统</h3>
        <a href="#" :class="{active: currentPage === 'list'}" @click.prevent="currentPage='list'; loadDrones()">无人机列表</a>
        <a href="#" @click.prevent="handleLogout">退出登录</a>
      </div>
      <div class="content">
        <div class="card">
          <div class="card-header"><h3>无人机信息列表</h3></div>
          <div class="card-body">
            <!-- 搜索栏 -->
            <div class="row mb-4">
              <div class="col-md-4"><select v-model="searchType" class="form-control">
                <option value="model">按型号搜索</option>
                <option value="uavCode">按编号搜索</option>
              </select></div>
              <div class="col-md-6"><input v-model="searchKeyword" type="text" class="form-control" placeholder="输入关键词" @keyup.enter="handleSearch"></div>
              <div class="col-md-2"><button class="btn btn-primary btn-block" @click="handleSearch">搜索</button></div>
            </div>
            <!-- 操作按钮 -->
            <div class="row mb-3">
              <div class="col-md-3"><button class="btn btn-success btn-block" @click="openAddModal">添加无人机</button></div>
              <div class="col-md-3"><button class="btn btn-info btn-block" @click="openAIGenModal">AI生成数据</button></div>
            </div>
            <!-- 无人机列表 -->
            <table class="table table-bordered table-striped">
              <thead><tr>
                <th>编号</th><th>型号</th><th>制造商</th><th>载重(kg)</th><th>高度(m)</th><th>续航(min)</th><th>状态</th><th>AI</th><th>操作</th>
              </tr></thead>
              <tbody><tr v-for="d in drones" :key="d.id">
                <td>{{d.uavCode}}</td><td>{{d.model}}</td><td>{{d.manufacturer}}</td><td>{{d.maxPayload}}</td><td>{{d.maxAltitude}}</td>
                <td>{{d.maxFlightTime}}</td><td>{{d.status===1?'正常':'停用'}}</td><td>{{d.aiGenerated===1?'是':'否'}}</td>
                <td>
                  <button class="btn btn-sm btn-primary" @click="openEditModal(d)">编辑</button>
                  <button class="btn btn-sm btn-danger ml-1" @click="deleteDrone(d.id)">删除</button>
                </td>
              </tr></tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- 添加/编辑模态框 -->
    <div v-if="showAddModal || showEditModal" class="modal-overlay" @click.self="closeModal()">
      <div class="modal">
        <div class="modal-header"><h5>{{currentDrone.id?'编辑':'添加'}}无人机</h5><button @click="closeModal()">&times;</button></div>
        <div class="modal-body">
          <div class="form-row">
            <div class="form-group col-md-6"><label>编号</label><input v-model="currentDrone.uavCode" type="text" class="form-control" required></div>
            <div class="form-group col-md-6"><label>型号</label><input v-model="currentDrone.model" type="text" class="form-control" required></div>
          </div>
          <div class="form-row">
            <div class="form-group col-md-6"><label>制造商</label><input v-model="currentDrone.manufacturer" type="text" class="form-control"></div>
            <div class="form-group col-md-6"><label>载重(kg)</label><input v-model="currentDrone.maxPayload" type="number" step="0.1" class="form-control"></div>
          </div>
          <div class="form-row">
            <div class="form-group col-md-6"><label>高度(m)</label><input v-model="currentDrone.maxAltitude" type="number" class="form-control"></div>
            <div class="form-group col-md-6"><label>续航(min)</label><input v-model="currentDrone.maxFlightTime" type="number" class="form-control"></div>
          </div>
          <div class="form-row">
            <div class="form-group col-md-6"><label>速度(m/s)</label><input v-model="currentDrone.maxSpeed" type="number" step="0.1" class="form-control"></div>
            <div class="form-group col-md-6"><label>翼展(cm)</label><input v-model="currentDrone.wingspan" type="number" step="0.1" class="form-control"></div>
          </div>
          <div class="form-row">
            <div class="form-group col-md-6"><label>自重(kg)</label><input v-model="currentDrone.weight" type="number" step="0.1" class="form-control"></div>
            <div class="form-group col-md-6"><label>状态</label><select v-model="currentDrone.status" class="form-control">
              <option :value="1">正常</option><option :value="0">停用</option>
            </select></div>
          </div>
          <div class="form-group"><label>备注</label><textarea v-model="currentDrone.remark" class="form-control" rows="3"></textarea></div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="closeModal()">取消</button>
          <button class="btn btn-primary" @click="saveDrone">保存</button>
        </div>
      </div>
    </div>

    <!-- AI生成模态框 -->
    <div v-if="showAIGenModal" class="modal-overlay" @click.self="showAIGenModal=false">
      <div class="modal">
        <div class="modal-header"><h5>AI生成数据</h5><button @click="showAIGenModal=false">&times;</button></div>
        <div class="modal-body"><div class="form-group"><label>生成数量</label><input v-model="aiCount" type="number" min="1" max="10" class="form-control"></div></div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showAIGenModal=false">取消</button>
          <button class="btn btn-info" @click="generateAI">生成</button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    /* 全局样式 */
    body { margin:0; padding:0; min-height:100vh; background:#f5f7fa; }
    /* 登录页样式 */
    .login-container { max-width:450px; margin:100px auto; padding:30px; background:white; border-radius:10px; box-shadow:0 0 20px rgba(0,0,0,0.1); }
    /* 主页面布局 */
    .main-container { min-height:100vh; display:flex; }
    /* 侧边栏样式 */
    .sidebar { width:250px; background:#2c3e50; color:white; padding:20px; min-height:100vh; }
    .sidebar a { color:#ecf0f1; display:block; padding:10px 15px; margin-bottom:5px; border-radius:4px; text-decoration:none; }
    .sidebar a:hover { background:#34495e; }
    .sidebar .active { background:#3498db; }
    /* 内容区样式 */
    .content { flex:1; padding:20px; }
    /* 模态框样式 */
    .modal-overlay { position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); display:flex; justify-content:center; align-items:center; z-index:1000; }
    .modal { background:white; border-radius:8px; width:90%; max-width:800px; max-height:90vh; overflow-y:auto; }
    .modal-header { display:flex; justify-content:space-between; align-items:center; padding:15px; border-bottom:1px solid #e9ecef; }
    .modal-header button { font-size:24px; border:none; background:none; cursor:pointer; }
    .modal-body { padding:15px; }
    .modal-footer { display:flex; justify-content:flex-end; padding:15px; border-top:1px solid #e9ecef; gap:10px; }
  `]
})

/**
 * 挂载 Vue 应用
 */
app.mount('#app')
