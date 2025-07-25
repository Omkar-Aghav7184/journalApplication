package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.entity.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.stream.Stream;

public class UserArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
       return Stream.of(
               Arguments.of(User.builder().username("ram").password("ram").build()),
               Arguments.of(User.builder().username("jay").password("").build())
       );
    }
}
