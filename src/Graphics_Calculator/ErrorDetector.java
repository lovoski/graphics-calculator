package Graphics_Calculator;

import java.util.Objects;

public class ErrorDetector {

    static final int BRACKET_NOT_EQUALS = 1;
    static final int NO_EQUAL_SIGN = 2;
    static final int ILLEGAL_FUNCTION = 3;
    static final int CORRECT = -1;

    /**
     * 一个用于检测当前输入的方程表达式是否合理的类
     * 不合理则在OutputPanel上打印信息
     * 提示实在哪一个方程的输入框存在问题
     */

    public ErrorDetector() {
    }

    public int judge(String input) {
        if (!Objects.equals(input, "")) {
            boolean equalSign;
            equalSign = input.charAt(input.length()-2)=='=' || input.charAt(1)=='=';
            if (!equalSign) return NO_EQUAL_SIGN;
            int leftCount = 0,rightCount = 0;
            for (int i = 0;i<input.length();i++) {
                if (input.charAt(i)=='(')
                    leftCount++;
                if (input.charAt(i)==')')
                    rightCount++;
            }
            if (leftCount!=rightCount) return BRACKET_NOT_EQUALS;
        }
        return CORRECT;
    }
}