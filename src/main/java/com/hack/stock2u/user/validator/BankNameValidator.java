package com.hack.stock2u.user.validator;

import com.hack.stock2u.constant.BankCode;
import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BankNameValidator implements ConstraintValidator<BankName, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return Arrays.stream(BankCode.values()).anyMatch(
        bankCode -> {
          String name = bankCode.getName();
          return name.equals(value);
        }
    );
  }
}
