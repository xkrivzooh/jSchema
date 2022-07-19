package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UnionSchema extends TypeSchema {
    private final List<TypeSchema> types;
    private final Map<String, Integer> indexByName;

    public UnionSchema(LockableArrayList<TypeSchema> types) {
      super(SchemaType.UNION);
      this.indexByName = new HashMap<>(Math.multiplyExact(2, types.size()));
      this.types = types.lock();
      int index = 0;
      for (TypeSchema type : types) {
        if (type.getType() == SchemaType.UNION) {
          throw new SchemaRuntimeException("Nested union: " + this);
        }
        String name = type.getFullName();
        if (name == null) {
          throw new SchemaRuntimeException("Nameless in union:" + this);
        }
        if (indexByName.put(name, index++) != null) {
          throw new SchemaRuntimeException("Duplicate in union:" + name);
        }
      }
    }

    @Override
    public List<TypeSchema> getTypes() {
      return types;
    }

    @Override
    public Integer getIndexNamed(String name) {
      return indexByName.get(name);
    }

    @Override
    public boolean equals(Object o) {
      if (o == this)
        return true;
      if (!(o instanceof UnionSchema))
        return false;
      UnionSchema that = (UnionSchema) o;
      return equalCachedHash(that) && types.equals(that.types) && propsEqual(that);
    }

    @Override
    int computeHash() {
      int hash = super.computeHash();
      for (TypeSchema type : types)
        hash += type.computeHash();
      return hash;
    }

    @Override
    public void addProp(String name, String value) {
      throw new SchemaRuntimeException("Can't set properties on a union: " + this);
    }

    @Override
    void toJson(Names names, JsonGenerator gen) throws IOException {
      gen.writeStartArray();
      for (TypeSchema type : types)
        type.toJson(names, gen);
      gen.writeEndArray();
    }
  }