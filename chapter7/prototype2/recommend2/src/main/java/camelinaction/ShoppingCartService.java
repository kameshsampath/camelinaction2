package camelinaction;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ShoppingCartService {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartService.class);

    // use rest template to call external REST service
    private final RestTemplate restTemplate = new RestTemplate();

    @HystrixCommand(fallbackMethod = "emptyCart")
    public String shoppingCart(String cartUrl, String id) {
        // get the current item in the shopping cart associated with the HTTP session id
        LOG.info("Calling cart service {}", cartUrl);
        CartDto[] carts = restTemplate.getForObject(cartUrl, CartDto[].class, id);
        String cartIds = cartsToCommaString(carts);
        LOG.info("Shopping cart items {}", cartIds);

        return cartIds;
    }

    public String emptyCart(String cartUrl, String id) {
        return "";
    }

    private String cartsToCommaString(CartDto[] carts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < carts.length; i++) {
            CartDto cart = carts[i];
            sb.append(cart.getItemId());
            if (i < carts.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
