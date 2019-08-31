@org.hibernate.annotations.GenericGenerator(
        name = "ID_GENERATOR",
        strategy = "enhanced-sequence",
        parameters = {
                @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TERYT_SEQUENCE"),
                @org.hibernate.annotations.Parameter(name = "increment_size", value = "1000"),
                @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
)
package com.flatrental;
