package ren.wenchao.jschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ren.wenchao.jschema.constraints.Constraint;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static ren.wenchao.jschema.JacksonUtils.safeGetTextValue;
import static ren.wenchao.jschema.JacksonUtils.safeGetValue;


public class FunctionSchemaParser {

    public static FunctionSchema parse(String functionSchemaString) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(functionSchemaString));

        FunctionSchemaBuilder builder = FunctionSchemaBuilder.builder();

        try {
            JsonNode root = JacksonUtils.MAPPER.readTree(functionSchemaString);
            JsonNode typeNode = root.get("type");
            Preconditions.checkState((typeNode != null)
                            && (typeNode.isTextual()) && typeNode.textValue().equals(FunctionSchema.TYPE),
                    "不是合法的FunctionSchema，缺少type或者type不正确");

            builder.namespace(safeGetTextValue(root, "namespace"));
            builder.name(safeGetTextValue(root, "name"));
            builder.functionName(safeGetTextValue(root, "functionName"));
            builder.doc(safeGetTextValue(root, "doc"));


            List<Parameter> parameters = Lists.newArrayList();
            JsonNode requestNode = root.get("request");
            parseRequestNode(requestNode, parameters);
            builder.request(parameters);

            JsonNode responseNode = root.get("response");
            TypeSchema responseTypeSchema = TypeSchemaParser.parse(responseNode.toString());
            builder.response(responseTypeSchema);
        } catch (IOException e) {
            throw new SchemaParseException(e);
        }

        return builder.build();
    }

    private static void parseRequestNode(JsonNode requestNode, List<Parameter> parameters) {
        if (requestNode == null || requestNode.isNull()) {
            return;
        }
        Preconditions.checkState(requestNode.isObject());
        ObjectNode objectNode = (ObjectNode) requestNode;

        Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            JsonNode requestParameterNode = entry.getValue();

            Map<String, String> props = Maps.newHashMap();
            parseParseNode(requestParameterNode.get("props"), props);

            List<Constraint> constraints = Lists.newArrayList();
            parseConstraintsNode(requestParameterNode.get("constraints"), constraints);


            ParameterBuilder parameterBuilder = ParameterBuilder.builder()
                    .name(entry.getKey())
                    .doc(safeGetTextValue(requestParameterNode, "doc"))
                    .props(props)
                    .addConstraints(constraints)
                    .schema(TypeSchemaParser.parse(safeGetValue(requestParameterNode, "type")));

            JsonNode defaultNode = requestParameterNode.get("default");
            if ((defaultNode != null) && (!defaultNode.isNull())) {
                parameterBuilder.defaultValue(safeGetTextValue(requestParameterNode, "default"));
            }
            parameters.add(parameterBuilder.build());
        }
    }

    private static void parseConstraintsNode(JsonNode constraintsNode, List<Constraint> constraints) {
        if (constraintsNode == null || constraintsNode.isNull()) {
            return;
        }

        Preconditions.checkState(constraintsNode.isObject());
        ObjectNode objectNode = (ObjectNode) constraintsNode;
        Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String constraintName = entry.getKey();
            JsonNode value = entry.getValue();
            Constraint constraint = Constraints.resolve(constraintName, value);
            if (constraint != null) {
                constraints.add(constraint);
            }
        }
    }


    private static void parseParseNode(JsonNode propsNode, Map<String, String> props) {
        if (propsNode == null || propsNode.isNull()) {
            return;
        }
        Preconditions.checkState(propsNode.isObject());
        ObjectNode objectNode = (ObjectNode) propsNode;
        Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            props.put(entry.getKey(), entry.getValue().asText());
        }
    }
}