package com.example.banking.entities;


import com.example.banking.utils.AccountName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


//@Table(name="subaccounts")
@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class SubAccount extends Account {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "superaccount_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Account superAccount;

    public SubAccount(Long id, Account account, String accountName) {
        super(id, accountName);
        this.superAccount = account;
    }

    public SubAccount(Long id, Account account) {
        super(id);
        this.superAccount = account;
    }

}
