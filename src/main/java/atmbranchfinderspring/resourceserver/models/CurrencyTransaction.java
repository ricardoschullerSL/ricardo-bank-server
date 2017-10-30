package atmbranchfinderspring.resourceserver.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class CurrencyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long currencyTransactionId;

    @ManyToOne
    @JoinColumn(name="sendingAccountId")
    private Account sendingAccount;
    @ManyToOne
    @JoinColumn(name="receivingAccountId")
    private Account receivingAccountId;

    private LocalDateTime executionDate;
    private long amountInCents;

    public CurrencyTransaction() {}

}
