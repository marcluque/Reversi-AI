package shared;

public class TestUtils {

    public static boolean mapEquals(char[][] actualMap, char[][] expectedMap) {
        for (int i = 0; i < actualMap.length; i++) {
            for (int j = 0; j < actualMap[0].length; j++) {
                if (expectedMap[i][j] != actualMap[i][j]) {
                    System.err.println("Expected " + expectedMap[i][j] + " at (" + j + ", " + i + ")");
                    System.err.println("Actual " + actualMap[i][j] + " at (" + j + ", " + i + ")");
                    return false;
                }
            }
        }

        return true;
    }
}
