package com.example.website;

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
@Controller //KuziControllerという名前のクラスを定義(Spring MVCのコントローラとして登録)
public class KuziController {
    @GetMapping("/kuzi")
    public String showKujiForm() {
        return "kuzi"; // /kuziへのgetリクエストがあった際に、（kuzi.html）を表示
    }
    @PostMapping("/kuzi") ///POSTリクエストがあった場合にフォームからのデータを受け取り、確率計算を行う。
    public String calcResult(@RequestParam int total,
                         @RequestParam int atari,
                         @RequestParam int buy,
                         Model model) {
                         double pA = 1.0 - (combination(total - atari, buy).doubleValue() / combination(total, buy).doubleValue()); // 1-ハズレを引く確率であたりの確率を計算
        
    
        Map<Integer, BigDecimal> probs = new HashMap<>();
        BigInteger combTotal = combination(total, buy); //全通りの組み合わせを計算しておく
        
        //n枚当たる確率を計算
        for (int n = 0; n <= Math.min(buy, atari); n++) {
            // 当たりからn枚引く
            BigInteger comb1 = combination(atari, n);
            // ハズレから残りを引く
            BigInteger comb2 = combination(total - atari, buy - n);

            // 確率計算(分子は「n枚あたりを引くパターン数*残りハズレを引くパターン数」分母は全パターン数。BigDecimalで小数点以下20桁の精度でとしRoundingMode.HALF_UPで四捨五入
            BigDecimal prob =  new BigDecimal(comb1.multiply(comb2)).divide(new BigDecimal(combTotal), 20, RoundingMode.HALF_UP);

            // 結果をマップに格納
            probs.put(n, prob.multiply(BigDecimal.valueOf(100)));
        }

        // モデルに確率を追加
        model.addAttribute("pA", pA * 100);
        model.addAttribute("probs", probs);
        model.addAttribute("total", total);
        model.addAttribute("atari", atari);
        model.addAttribute("buy", buy);
        return "kuzi";
    }
     // nCrを計算するメソッド(計算結果が大きくなる可能性があるため、任意精度の整数を扱えるBigIntegerを使用)
    public static BigInteger combination(int n, int r) {
    if (r < 0 || r > n) return BigInteger.ZERO; //例外処理
    if (r == 0 || r == n) return BigInteger.ONE; //計算が不要な場合の戻り値

    r = Math.min(r, n - r); //組み合わせ計算の対象性を利用して計算量を削減
    BigInteger result = BigInteger.ONE; //値の初期値
    for (int i = 1; i <= r; i++) {
        result = result.multiply(BigInteger.valueOf(n - i + 1)).divide(BigInteger.valueOf(i));
    }
    return result;
    }

}


