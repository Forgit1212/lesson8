package org.example.metod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class MainApp {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList(" A",  " AB",  " B") );
        Stream<String> stream = list.stream(); //Стримы создаются из различных источников данных,
        // но в большинстве случаев — из коллекций List и Set с помощью метода stream().

        Stream<Integer> stream1 = Stream.of(1 ,  2 ,  3 ,  4 ); //Для создания стрима, состоящего
        // из произвольного набора элементов, можно использовать статический метод Stream.of()

        String[] array = {" A",  " B",  " C"} ;
        Stream<String> stream2 = Arrays.stream(array);
        Stream<String> anotherStream = Stream.of(array);

        //можно создавать «специализированные» стримы (IntStream, DoubleStream, LongStream):
        IntStream intStream = IntStream.of(1, 2, 3, 4);
        LongStream longStream = LongStream.of(1L, 2L, 3L, 4L);
        IntStream rangedIntStream = IntStream.rangeClosed(1, 100);
        //В IntStream также можно преобразовать обычный стрим
        IntStream intStream1 = Stream.of(1, 2, 3, 4) .mapToInt(n -> n);

        //--- Терминальные операции ---

        //С помощью стандартных Collectors.toList() и Collectors.toSet()  можно легко получать
        // коллекции типа List и Set
        Stream<String> stream3 = Stream.of(" A",  " B",  " C") ;
        List<String> list1 = stream3.collect(Collectors.toList());
        Set<String> set = stream3.collect(Collectors.toSet());

        //Кроме toList() и toSet() Collectors позволяют создавать подгруппы объектов из стрима
        //для общей цели. Например, можно определить среднюю длину слова в стриме строк:
        String[] array1 = {" Aaa",  " Bbbbb",  " Cc"} ;
        System.out.println(Arrays.stream(array1)
                .collect(Collectors.averagingInt(s -> s.length()))); // Результат: 3.3333333333333335

        //Можно выбрать строки по определённому признаку и вывести строкой:
        String[] array2 = {"Aaa",  "Bbbbb",  "Cc",  "Aa"};
        System.out.println(Arrays.stream(array2)
                .filter(str -> str.startsWith("A") )
                .collect(Collectors.joining(" и ", "Перечисленные слова [", "]" +
                        "начинаются на букву A") ));
        // Перечисленные слова [Aaa и Aa] начинаются на букву A

        //Вторая терминальная операция — forEach(), её задача заключается в выполнении указанного
        //действия для каждого элемента стрима. В примере ниже мы пройдём по каждому элементу стрима и
        //отпечатаем его в консоль:

        Stream<String> stream4 = Stream.of(" A",  " B",  " C") ;
        stream1.forEach(str -> System.out.println(str));

        Stream<String> stream5 = Stream.of(" A",  " B",  " C") ;
        stream2.forEach(System.out::println);

        //Операция count() возвращает количество элементов в стриме.

        //Операция reduce() выполняет роль сумматора по всем элементам стрима.
        Stream<Integer> stream6 = Stream.of(1, 2, 3, 24, 5, 6) ;
        stream6.reduce((i1, i2) -> i1 > i2 ? i1 : i2)
                .ifPresent(System.out::println);


        // --- Промежуточные операции ---

        //Операция filter() позволяет отфильтровать элементы стрима по заданному правилу. В примере ниже
        //мы пропускаем через фильтр только чётные числа:
        Stream<Integer> stream7 = Stream.of(1, 2, 3, 4, 5, 6);
        stream7.filter(n -> n % 2   == 0 ) .forEach(System.out::print); //Результат: 246

        //Операция distinct() позволяет преобразовать стрим в множество уникальных объектов (по аналогии с
        //работой Set):
        Stream.of("A", "A", "A", "B", "B", "B", "B")
                .distinct()
                .forEach(System.out::print); // AB

        //Операция map() служит для преобразования типа объектов внутри стрима. В примере ниже мы
        //преобразуем Stream<String> в Stream<Integer> путем вызова у каждого строкового объекта
        //исходного стрима метода length(). Тем самым мы преобразуем набор слов в набор длин этих слов:
        Stream<String> stream8 = Stream.of(" Java",  " Core",  " ABC") ;
        stream8.map(str -> str.length()).forEach(System.out::print); // 443

        // --- Описание базовых функциональных интерфейсов ---

        //Function представляет собой функция, которая принимает на вход аргумент типа T и возвращает
        //(производит) объект типа R. (Например, в операции map данная функция позволяет привести один
        //объект к другому):
        /*@FunctionalInterface
        public interface Function<T, R> {
            R apply(T t);
        }

        //Predicate представляет собой предикат (функцию, возвращающую boolean) для одного аргумента.
        //(Например, для операции filter мы прописываем предикат, определяющий критерий для
        // отфильтровывания объектов):
        @FunctionalInterface
        public interface Predicate<T> {
            boolean test(T t);
        }

        //Consumer представляет операцию, которая принимает на вход один аргумент и не возвращает
        //никакого результата. (Например, в операции forEach):
        @FunctionalInterface
        public interface Consumer<T> {
            void accept(T t);
        }

        //Supplier представляет собой поставщик результатов. В примере Supplier будет возвращать
        //числа в пределах от 0 до 99:
        @FunctionalInterface
        public interface Supplier<T> {
            T get();
        }
        // Пример
        Stream.generate(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return (int) (Math.random() * 1 00);
            }
        }

        //BinaryOperator представляет собой операцию, принимающую на вход два аргумента одного и того же
        //типа, и возвращающая результат того же типа. (Например, используется в reduce()):
        @FunctionalInterface
        public interface BinaryOperator<T> extends BiFunction<T, T, T> {
        T apply(T t1, T t2);
        }*/


        // --- Порядок обработки ---

        // --- Параллельные стримы ---

    }
}
