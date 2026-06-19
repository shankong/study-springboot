// 用户数据类型，和后端 Account.java 对应
export interface Account {
  accountId: string   // Long → String，避免 JS 精度丢失
  accountEmail: string
  accountUsername: string
  accountPassword: string
  accountState: number
}

// 状态标签映射
export function getStateTag(state: number) {
  switch (state) {
    case 1: return { text: '正常', type: 'success' as const }
    case 0: return { text: '禁用', type: 'danger' as const }
    default: return { text: '未知', type: 'info' as const }
  }
}
