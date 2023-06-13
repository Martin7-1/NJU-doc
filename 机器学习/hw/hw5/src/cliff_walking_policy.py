# 策略迭代算法
import copy


class CliffWalkingEnv:
    """
    悬崖漫步环境
    """

    def __init__(self, col_num=12, row_num=4):
        # 定义网格世界的列
        self.col_num = col_num
        # 定义网格世界的行
        self.row_num = row_num
        # 转移矩阵 transition_matrix[state][action] = [(p, next_state, reward, done)] 包含下一个状态和奖励
        self.transition_matrix = self.create_transition()

    def create_transition(self):
        # init
        transition = [[[] for _ in range(4)] for _ in range(self.row_num * self.col_num)]
        # 四种动作，change[0]: 上; change[1]: 下; change[2]: 左; change[3]: 右
        change = [[0, -1], [0, 1], [-1, 0], [1, 0]]
        for i in range(self.row_num):
            for j in range(self.col_num):
                for a in range(4):
                    # 位置在悬崖或者目标状态，因为无法继续交互，任何动作奖励都为 0
                    if i == self.row_num - 1 and j > 0:
                        transition[i * self.col_num + j][a] = [(1, i * self.col_num + j, 0, True)]
                        continue
                    # 其他位置
                    next_x = min(self.col_num - 1, max(0, j + change[a][0]))
                    next_y = min(self.row_num - 1, max(0, i + change[a][1]))
                    next_state = next_y * self.col_num + next_x
                    reward = -1
                    done = False
                    # 下一个位置在悬崖或终点
                    if next_y == self.row_num - 1 and next_x > 0:
                        done = True
                        # 下一个位置在悬崖
                        if next_x != self.col_num - 1:
                            reward = -100
                    transition[i * self.col_num + j][a] = [(1, next_state, reward, done)]

        return transition


class PolicyIteration:
    """
    策略迭代算法
    """

    def __init__(self, env: CliffWalkingEnv, theta, gamma):
        self.env = env
        # 初始化价值为 0
        self.val = [0] * self.env.col_num * self.env.row_num
        # 初始化为均匀随机策略
        self.pi = [[0.25, 0.25, 0.25, 0.25] for _ in range(self.env.col_num * self.env.row_num)]
        self.theta = theta
        self.gamma = gamma

    def calc_qsa(self, s, is_eval):
        qsa_list = []
        for a in range(4):
            qsa = 0
            for res in self.env.transition_matrix[s][a]:
                p, next_state, r, done = res
                qsa += p * (r + self.gamma * self.val[next_state] * (1 - done))
            if is_eval:
                qsa_list.append(self.pi[s][a] * qsa)
            else:
                qsa_list.append(qsa)
        return qsa_list

    def policy_evaluation(self):
        cnt = 1
        while True:
            max_diff = 0
            new_val = [0] * self.env.col_num * self.env.row_num
            for s in range(self.env.col_num * self.env.row_num):
                # 开始计算状态 s 下的所有 Q(s, a) 价值
                qsa_list = self.calc_qsa(s, True)
                new_val[s] = sum(qsa_list)
                max_diff = max(max_diff, abs(new_val[s] - self.val[s]))
            self.val = new_val
            if max_diff < self.theta:
                break
            cnt += 1
        print("策略评估进行 %d 轮后完成" % cnt)

    def policy_improvement(self):
        for s in range(self.env.row_num * self.env.col_num):
            qsa_list = self.calc_qsa(s, False)
            max_qsa = max(qsa_list)
            # 计算有几个动作得到了最大的 Q 值
            cnt_qsa = qsa_list.count(max_qsa)
            # 让这些动作均分概率
            self.pi[s] = [1 / cnt_qsa if q == max_qsa else 0 for q in qsa_list]
        print("策略提升完成")
        return self.pi

    def policy_iteration(self):
        while True:
            self.policy_evaluation()
            # 深拷贝，方便比较，不会改变指向的值
            old_pi = copy.deepcopy(self.pi)
            new_pi = self.policy_improvement()
            if old_pi == new_pi:
                break


def print_agent(agent, action_meaning, disaster=None, end=None):
    if end is None:
        end = []
    if disaster is None:
        disaster = []
    print("状态价值: ")
    for i in range(agent.env.row_num):
        for j in range(agent.env.col_num):
            print('%6.6s' % ('%.3f' % agent.val[i * agent.env.col_num + j]), end=' ')
        print()

    print("策略: ")
    for i in range(agent.env.row_num):
        for j in range(agent.env.col_num):
            tmp_val = i * agent.env.col_num + j
            if tmp_val in disaster:
                print("****", end=' ')
            elif tmp_val in end:
                print("EEEE", end=' ')
            else:
                a = agent.pi[tmp_val]
                pi_str = ''
                for k in range(len(action_meaning)):
                    pi_str += action_meaning[k] if a[k] > 0 else 'o'
                print(pi_str, end=' ')
        print()


if __name__ == '__main__':
    env = CliffWalkingEnv()
    action_meaning = ['^', 'v', '<', '>']
    theta = 0.001
    gamma = 0.9
    agent = PolicyIteration(env, theta, gamma)
    agent.policy_iteration()
    print_agent(agent, action_meaning, list(range(37, 47)), [47])
