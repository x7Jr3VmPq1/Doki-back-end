package com.megrez.dokibackend;

public class Constant {
    public static final String MappingString = "{\n" +
            "  \"mappings\":{\n" +
            "    \"properties\": {\n" +
            "      \"info\":{\n" +
            "        \"type\": \"text\", \n" +
            "        \"analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"email\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"object\",\n" +
            "        \"properties\": {\n" +
            "          \"fn\": { \"type\": \"keyword\" },\n" +
            "          \"ln\": { \"type\": \"keyword\" }\n" +
            "        }\n" +
            "      },\n" +
            "      \"phoneNumber\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
