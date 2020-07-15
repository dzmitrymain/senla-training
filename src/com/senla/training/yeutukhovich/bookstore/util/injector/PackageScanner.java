package com.senla.training.yeutukhovich.bookstore.util.injector;

import com.senla.training.yeutukhovich.bookstore.exception.InternalException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PackageScanner {

    private static Set<Class<?>> classes;

    private PackageScanner() {

    }

    public static Set<Class<?>> findSingletons(String packageName) {
        return findClasses(packageName).stream()
                .filter(clazz ->
                        clazz.isAnnotationPresent(Singleton.class))
                .collect(Collectors.toSet());

    }

    // приватный метод возвращает значение приватного поля этого же класса - это поле и так доступно
    // пусть лучше поле инициализируется сразу в поле, а заполнение сета происходит в методе инит
    // название инит будет намекать на однократный вызов
    // если я не ошибаюсь, это будет уместно сделать из статик блока
    private static Set<Class<?>> findClasses(String packageName) {
        classes = new HashSet<>();
        scanPackage(packageName);
        return classes;
    }

    private static void scanPackage(String packageName) {
        URL contextUrl = Thread.currentThread().getContextClassLoader().getResource(packageName);
        if (contextUrl == null) {
            // возможно, стоит подумать о выбросе исключения или еще каком-то оповещении, потому что потом
            // скорее всего где-то будет NPE
            // ситуация в этом блоке невозможная, но все же, если решил обработать - обработай так,
            // чтобы была возможность потом понять, где проблема, потому что НПЕ будет далеко отсюда
            return;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) contextUrl.getContent()))) {
            br.lines().forEach(line -> {
                if (line.endsWith(".class")) {
                    try {
                        // видел рекомендации использовать replace() вместо replaceAll()
                        // что-то связано с лучшей работой с регулярками, проверь, должно быть норм с replace
                        classes.add(Class.forName(packageName.replaceAll("/", ".")
                                + line.substring(0, line.lastIndexOf('.'))));
                    } catch (ClassNotFoundException e) {
                        throw new InternalException(e.getMessage());
                    }
                }
                if (!line.contains(".")) {
                    // я не фанат рекурсии, если есть возможность и желание - перепиши без нее,
                    // если нет - оставляй, это чисто моя придирка
                    scanPackage(packageName + line + "/");
                }
            });
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
    }
}
