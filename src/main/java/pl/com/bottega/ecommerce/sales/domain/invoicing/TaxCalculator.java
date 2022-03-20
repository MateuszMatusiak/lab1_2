package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.math.BigDecimal;

public class TaxCalculator implements Tax {

    private Money amount;

    private String description;

    private RequestItem item;

    public TaxCalculator(Money amount, String description) {
        super();
        this.amount = amount;
        this.description = description;
    }

    public TaxCalculator(RequestItem item) {
        this.item = item;
    }

    @Override
    public Money getAmount() {
        return amount;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public InvoiceLine calculate() {
        Money net = item.getTotalCost();
        BigDecimal ratio = null;
        String desc = null;

        switch (item.getProductData().getType()) {
            case DRUG:
                ratio = BigDecimal.valueOf(0.05);
                desc = "5% (D)";
                break;
            case FOOD:
                ratio = BigDecimal.valueOf(0.07);
                desc = "7% (F)";
                break;
            case STANDARD:
                ratio = BigDecimal.valueOf(0.23);
                desc = "23%";
                break;

            default:
                throw new IllegalArgumentException(item.getProductData().getType() + " not handled");
        }

        this.amount = net.multiplyBy(ratio);
        this.description = desc;

        return new InvoiceLine(item.getProductData(), item.getQuantity(), net, this);
    }
}
