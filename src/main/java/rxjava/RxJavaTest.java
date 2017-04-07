package rxjava;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

// Coding exercise to learn RxJava
// Code from
// http://blog.danlew.net/2014/09/15/grokking-rxjava-part-1/
// and
//  Reactive Programming with RxJava

public class RxJavaTest {


    public static void main(String[] arg) {


        example4();
        //example3b();
        //example3a();
        //example3("Eric");
        //example2b();
        //example2a();
        // example2();
        //example1c();
        //example1b();
        //example1a();
        //example1();

    }


    // Observable checks if subscriber has unsubscribed before emitting value
    private static void example4() {
        Observable<Integer> observable = Observable.create(s -> {
            for (int i = 0; i < 10; ++i) {
                if (s.isUnsubscribed()) {  // check if subscriber still subscribed
                    break;
                }
                s.onNext(i);
            }
            s.onCompleted();
        });

        Subscriber<Integer> intSubscriber = new Subscriber<Integer>() {
            @Override
            public void onNext(Integer i) {
                System.out.println("Number: "+ i);
                if (i > 5) {
                    unsubscribe();  // Unsubscribe of coarse!
                }
            }
            @Override
            public void onCompleted() {
                System.out.println("All done");
            }

            @Override
            public void onError(Throwable e) {
            }
        };


        observable.subscribe(intSubscriber);

    }

    // String multiple operators together
    private static void example3b() {
        Observable.just("Hello, world!")
                .map(s -> s + " -Eric")
                .map(s -> s.hashCode())
                .map(i -> Integer.toString(i))
                .subscribe(s -> System.out.println(s));
    }

    // Transform an integer to a string using lambda
    // Observable function  pipeline (in this case 'map') is (generally) synchronous by default
    private static void example3a() {
        Observable<Integer> o = Observable.create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onCompleted();
        });

        o.map(i -> " Number:" + i)
                .subscribe(s -> System.out.println(s));

    }

    // Use map to transform emitted value to something else
    private static void example3(String name) {
        Observable.just("Hello, world! ")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + name;
                    }
                })
                .subscribe(s -> System.out.println(s));
    }

    // Using lambdas for onNext, onError, onComplete
    private static void example2b() {
        Observable.create(s -> {
            s.onNext("Hello World!!");
            s.onCompleted();

        }).subscribe(System.out::println
                , Throwable::printStackTrace
                , () -> noMore());
    }

    private static void noMore() {
        System.out.println("All Done");
    }

    // Using lambdas for onNext, onError, onComplete
    private static void example2a() {
        Observable.create(s -> {
            s.onNext("Hello World!!");
            s.onCompleted();

        }).subscribe(hello -> System.out.println(hello)   // onNext
                , (Throwable t) -> {                       // onError
                    t.printStackTrace();
                }
                , () -> {                                   //onComplete
                    System.out.println("All Done!");
                });

    }


    // Added onError and onCompleted Actions to subscriber
    private static void example2() {

        Action1<Object> onNextAction = new Action1<Object>() {
            @Override
            public void call(Object s) {
                System.out.println(s);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable t) {
                System.out.println(t.toString());
            }
        };

        Action0 onCompleteAction = new Action0() {
            @Override
            public void call() {
                System.out.println("All Done!");

            }
        };

        Observable.create(s -> {
            s.onNext("Hello World!!");
            s.onCompleted();

        }).subscribe(onNextAction, onErrorAction, onCompleteAction);

        Observable<String> myObservable =
                Observable.just("Hello, world!");
        myObservable.subscribe(onNextAction, onErrorAction, onCompleteAction);
    }


    // Same as example1a and b but using lambda
    private static void example1c() {
        Observable.just("Hello, world!").subscribe(s -> System.out.println(s));

    }

    // Same as example1a but chaining method calls together
    private static void example1b() {

        Observable.just("Hello, world!")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
    }

    // Use 'just' - emit 1 value then quit (onComplete)
    private static void example1a() {
        Observable<String> myObservable =
                Observable.just("Hello, world!");

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        };

        // Note that only onNext is mandatory, onComplete and onError are optional
        myObservable.subscribe(onNextAction);
    }

    // Print hello world with just about most code you can use.
    private static void example1() {
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello, world!");
                        sub.onCompleted();
                    }
                }
        );

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onCompleted() {
                System.out.println("All done");
            }

            @Override
            public void onError(Throwable e) {
            }
        };

        myObservable.subscribe(mySubscriber);
    }
}
