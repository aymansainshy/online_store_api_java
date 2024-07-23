# Online Store Api Java (Spring Boot)

## Dependencies :

* Project Lombok : For code generations.


* In Spring , the default scope of a bean is singleton, which means that Spring will create a single instance of the
  bean and will share it across the entire application context, However, if you want to explicitly specify that a bean
  should be singleton, you can use the @Scope annotation with the <sub> ConfigurableBeanFactory.SCOPE_SINGLETON </sub>
  constant.

``` 
    public interface MyService {
        void performAction();
    }
    
    @Service
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) 
    public class MyServiceImpl implements MyService {
        @Override
        public void performAction() {
            System.out.println("Action performed by MyServiceImpl");
        }
    }

```

