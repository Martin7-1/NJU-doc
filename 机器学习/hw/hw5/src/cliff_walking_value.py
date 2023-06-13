# 价值迭代算法
from cliff_walking_policy import CliffWalkingEnv
from cliff_walking_policy import print_agent


class ValueInteration:
    """价值迭代算法"""

    def __init__(self, env: CliffWalkingEnv, theta, gamma):
        self.env = env
        # 初始化价值为 0
        self.val = [0] * self.env.col_num * self.env.row_num
        # 价值收敛阈值
        self.theta = theta
        self.gamma = gamma
        # 价值迭代结束后得到的策略
        self.pi = [None for _ in range(self.env.col_num * self.env.row_num)]

    def value_iteration(self):
        cnt = 0
        while True:
            max_diff = 0
            new_val = [0] * self.env.col_num * self.env.row_num
            for s in range(self.env.col_num * self.env.row_num):
                # 开始计算状态 s 下的所有 Q(s, a) 价值
                qsa_list = []
                for a in range(4):
                    qsa = 0
                    for res in self.env.transition_matrix[s][a]:
                        p, next_state, r, done = res
                        qsa += p * (r + self.gamma * self.val[next_state] * (1 - done))
                    # 这一行和下一行是价值迭代和策略迭代的主要区别
                    qsa_list.append(qsa)
                new_val[s] = max(qsa_list)
                max_diff = max(max_diff, abs(new_val[s] - self.val[s]))
            self.val = new_val
            if max_diff < self.theta:
                break
            cnt += 1
        print("价值迭代一共进行 %d 轮" % cnt)
        self.get_policy()

    def get_policy(self):
        # 根据价值函数导出一个贪婪策略
        for s in range(self.env.row_num * self.env.col_num):
            qsa_list = []
            for a in range(4):
                qsa = 0
                for res in self.env.transition_matrix[s][a]:
                    p, next_state, r, done = res
                    qsa += p * (r + self.gamma * self.val[next_state] * (1 - done))
                qsa_list.append(qsa)
            max_qsa = max(qsa_list)
            cnt_qsa = qsa_list.count(max_qsa)
            # 均分概率
            self.pi[s] = [1 / cnt_qsa if q == max_qsa else 0 for q in qsa_list]


if __name__ == '__main__':
    env = CliffWalkingEnv()
    action_meaning = ['^', 'v', '<', '>']
    theta = 0.001
    gamma = 0.9
    agent = ValueInteration(env, theta, gamma)
    agent.value_iteration()
    print_agent(agent, action_meaning, list(range(37, 47)), [47])
