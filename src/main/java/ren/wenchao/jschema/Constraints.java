package ren.wenchao.jschema;

import com.fasterxml.jackson.databind.JsonNode;
import ren.wenchao.jschema.constraints.*;

public class Constraints {
    public static Constraint resolve(String constraintName, JsonNode value) {
        if ("AssertFalse".equals(constraintName)) {
            return AssertFalse.resolve(value);
        } else if ("AssertTrue".equals(constraintName)) {
            return AssertTrue.resolve(value);
        } else if ("DecimalMax".equals(constraintName)) {
            return DecimalMax.resolve(value);
        } else if ("DecimalMin".equals(constraintName)) {
            return DecimalMin.resolve(value);
        }
//        else if ("IntRange".equals(constraintName)) {
//            return IntRange.resolve(value);
//        }
        else if ("Max".equals(constraintName)) {
            return Max.resolve(value);
        } else if ("Min".equals(constraintName)) {
            return Min.resolve(value);
        } else if ("NotNull".equals(constraintName)) {
            return NotNull.resolve(value);
        } else if ("Null".equals(constraintName)) {
            return Null.resolve(value);
        }
        throw new RuntimeException("unKnow constraintName:" + constraintName);
    }
}