package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public interface Constraint {

    void toJson(JsonGenerator gen) throws IOException;
}
