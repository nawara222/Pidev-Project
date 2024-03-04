package Services.OrdersAndBaskets;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import java.util.HashMap;
import java.util.Map;

public class StripeService {
    public void init() {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY"); // Fetch your secret key securely
    }

    public Charge charge(String token, long amount) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", "usd");
        params.put("description", "Example charge");
        params.put("source", token);

        return Charge.create(params);
    }
}
