import axios from 'axios'
import {APP_REQUEST_BASE_URL} from './basic-constants'

const req = axios.create({
    baseURL: APP_REQUEST_BASE_URL,
    timeout: 15000,
})

// 请求拦截器
req.interceptors.request.use(
    (config) => config,
    () => {
        console.error('请求异常')
    },
)

// 响应拦截器
req.interceptors.response.use((response) => {
    const d = response.data || {}
    if (d.code !== '0' && typeof window !== 'undefined') {
        // 接口报错处理
        alert(d.description)
        return null
    }
    return d.data
})

export default req
