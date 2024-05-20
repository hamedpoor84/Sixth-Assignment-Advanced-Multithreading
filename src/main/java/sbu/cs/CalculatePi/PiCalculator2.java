package sbu.cs.CalculatePi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PiCalculator2 {


    public static class CalculatePi implements Runnable {
        MathContext mc = new MathContext(1000);     // use when calling BigDecimal operations like divide or multiply
        int n = 1000;
        public CalculatePi(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            BigDecimal sign = new BigDecimal(1);
            if (n % 2 == 0)
            {
                sign = new BigDecimal(-1);
            }
            BigDecimal numerator = new BigDecimal(4).divide(new BigDecimal((2*n)*(2*n + 1)*(2*n + 2)) , mc).multiply(sign , mc) ;

            addTouSum(numerator);
        }

        public BigDecimal factorial(int n){
            BigDecimal temp = new BigDecimal(1);
            for (int i = 1; i <= n; i++) {
                temp = temp.multiply(new BigDecimal(i), mc);
            }

            return temp;
        }
    }

    public static BigDecimal sum;
    public String calculate(int floatingPoint)
    {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);

        sum = new BigDecimal(0);
        addTouSum(BigDecimal.valueOf(3));
        for (int i = 1; i < 10000; i++) {                          // increasing the number of iterations improves
            PiCalculator2.CalculatePi task = new PiCalculator2.CalculatePi(i);          // accuracy, try 200 and see the difference!
            threadPool.execute(task);
        }

        threadPool.shutdown();      // always call before awaitTermination

        try {
            threadPool.awaitTermination(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sum = sum.setScale(floatingPoint, RoundingMode.HALF_DOWN);

        return sum.toString();
    }
    public static synchronized void addTouSum(BigDecimal value){
        sum = sum.add(value);
    }
    public static void main(String[] Args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(8);

        for (int i = 1; i < 1000; i++)  {                          // increasing the number of iterations improves
            PiCalculator2.CalculatePi task = new PiCalculator2.CalculatePi(i);          // accuracy, try 200 and see the difference!
            threadPool.execute(task);
        }

        threadPool.shutdown();      // always call before awaitTermination

        try {
            threadPool.awaitTermination(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // the accurate value of sin(0.01) up to 1000 decimal places
        BigDecimal accurateValue = new BigDecimal("3.14159265358979323846264338327950288419716939937510" +
                "58209749445923078164062862089986280348253421170679" +
                "82148086513282306647093844609550582231725359408128" +
                "48111745028410270193852110555964462294895493038196" +
                "44288109756659334461284756482337867831652712019091" +
                "45648566923460348610454326648213393607260249141273" +
                "72458700660631558817488152092096282925409171536436" +
                "78925903600113305305488204665213841469519415116094" +
                "33057270365759591953092186117381932611793105118548" +
                "07446237996274956735188575272489122793818301194912" +
                "98336733624406566430860213949463952247371907021798" +
                "60943702770539217176293176752384674818467669405132" +
                "00056812714526356082778577134275778960917363717872" +
                "14684409012249534301465495853710507922796892589235" +
                "42019956112129021960864034418159813629774771309960" +
                "51870721134999999837297804995105973173281609631859" +
                "50244594553469083026425223082533446850352619311881" +
                "71010003137838752886587533208381420617177669147303" +
                "59825349042875546873115956286388235378759375195778" +
                "18577805321712268066130019278766111959092164201989");

        sum = sum.setScale(1000, RoundingMode.HALF_DOWN);
        accurateValue = accurateValue.setScale(1000, RoundingMode.HALF_DOWN);

        System.out.println("Pi up to 1000 decimal places:");
        System.out.println("Calculated Value:  " + sum);
        System.out.println("Accurate Value:    " + accurateValue);

        // note that a BigDecimal object may be printed as a number in the scientific notation. To print a normal
        // decimal number use the toPlainString() method.
        System.out.println("Difference:        " + accurateValue.subtract(sum).abs().toPlainString());

    }
}
