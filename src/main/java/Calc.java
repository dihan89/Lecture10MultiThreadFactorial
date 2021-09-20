public class Calc {
        public long calc( int n){
        if (n < 0)
            throw new IllegalArgumentException(Integer.toString(n));
        long result = 1;
        for (int i = 2; i < n + 1; ++i )
            result *= i;
        return result;
    }

    public void print(int n, long result){
        System.out.println(Thread.currentThread().getName()+ " "+ n+"! = " +result);
    }

}
