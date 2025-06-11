package com.example.counter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class KuziController {
    @GetMapping("/kuzi")
    public String showKujiForm() {
        return "kuzi"; // ← くじフォームのHTML（kuzi.html）を表示
    }
    @PostMapping("/kuzi")
    public String calcResult(@RequestParam int total,
                         @RequestParam int a,
                         @RequestParam int buy,
                         Model model) {
                         double pA = 1.0 - (combination(total - a, buy).doubleValue() / combination(total, buy).doubleValue());
                         System.out.println(pA);
        
    
        Map<Integer, BigDecimal> probs = new HashMap<>();
        BigInteger combTotal = combination2(total, buy);

        for (int n = 0; n <= Math.min(buy, a); n++) {
            // 当たりからn枚引く
            BigInteger comb1 = combination2(a, n);
            // ハズレから残りを引く
            BigInteger comb2 = combination2(total - a, buy - n);

            // 確率計算
            BigDecimal numerator = new BigDecimal(comb1.multiply(comb2));
            BigDecimal prob = numerator.divide(new BigDecimal(combTotal), 20, RoundingMode.HALF_UP);

            // 結果をマップに格納
            probs.put(n, prob.multiply(BigDecimal.valueOf(100)));

            // ちょうどn枚当たる確率を表示
            System.out.printf("ちょうど %d 枚当たる確率: %.10f\n", n, prob);
        }
            System.out.println("あたりが出る確率 = " + pA);

        // モデルに確率を追加
        model.addAttribute("pA", pA * 100);
        model.addAttribute("probs", probs);
        model.addAttribute("total", total);
        model.addAttribute("a", a);
        model.addAttribute("buy", buy);
        return "kuzi";
    }
    public static BigInteger combination(int n, int r) {
        if (r > n) return BigInteger.ZERO;
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= r; i++) {
            result = result.multiply(BigInteger.valueOf(n - i + 1));
            result = result.divide(BigInteger.valueOf(i));
        }
        return result;
    }
    public static BigInteger combination2(int n, int r) {
    if (r < 0 || r > n) return BigInteger.ZERO;
    if (r == 0 || r == n) return BigInteger.ONE;

    r = Math.min(r, n - r);
    BigInteger result = BigInteger.ONE;
    for (int i = 1; i <= r; i++) {
        result = result.multiply(BigInteger.valueOf(n - i + 1));
        result = result.divide(BigInteger.valueOf(i));
    }
    return result;
    }

}


