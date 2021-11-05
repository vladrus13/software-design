package program;

import aspects.AspectProfile;

public class Fibonacci {
    @AspectProfile
    public static long f(String login, int n) {
        if (n < 2) return 1;
        return f(login, n - 1) + f(login, n - 2);
    }
}
