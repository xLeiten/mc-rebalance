package me.xleiten.rebalance.api.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import org.jetbrains.annotations.NotNull;

public interface IType<T, E extends JsonElement>
{
    @NotNull String getTypeKey();

    @NotNull Class<T> getTypeClass();

    @NotNull T deserialize(E element, JsonDeserializationContext context);

    @NotNull E serialize(T object, JsonSerializationContext context);

}
