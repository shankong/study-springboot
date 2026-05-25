import axios from "axios";
import { ElMessage } from "element-plus";

const defaultError = () => ElMessage.error('出现了点小问题，请稍后再试')
const defaultFailure = (message) => ElMessage.error(message)

function post(url, data, success, failure = defaultFailure, error = defaultError) {
    // 把 JS 对象转成 URL 编码格式（application/x-www-form-urlencoded）
    const formData = new URLSearchParams()
    for (const key in data) {
        formData.append(key, data[key])
    }
    axios.post(url, formData, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        withCredentials: true
    }).then(({ data }) => {
        if (data.success) {
            success(data.message, data.status)
        } else {
            failure(data.message, data.status)
        }
    }).catch(error)
}

function get(url, success, failure = defaultFailure, error = defaultError) {
    axios.get(url, {
        withCredentials: true
    }).then(({ data }) => {
        if (data.success) {
            success(data.message, data.status)
        } else {
            failure(data.message, data.status)
        }
    }).catch(error)
}

export {
    post,
    get
}
