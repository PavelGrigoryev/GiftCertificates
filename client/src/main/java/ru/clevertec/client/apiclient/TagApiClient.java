package ru.clevertec.client.apiclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.client.dto.TagDto;

@FeignClient(name = "TagApiClient", url = "${base.url.tags}")
public interface TagApiClient {

    @GetMapping("/{id}")
    TagDto findTagById(@PathVariable Long id);

}
