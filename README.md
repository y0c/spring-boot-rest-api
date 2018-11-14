# Spring Boot 기반 REST API Study

[그런 REST API로 괜찮은가 ?](https://www.youtube.com/watch?v=RP_f5dMoHFc)

[Spring 기반 Rest API 개발](https://www.slideshare.net/whiteship/rest-api-development-with-spring?fbclid=IwAR0wnUarkLf73P4EZtPyA_ikKcuuG-cWDs6xDxcrlwfqLoLxtZmOJEYcA7c)

위 두개의 발표를 참조한 학습용 Repository 

## HATEOAS 

> REST API는 HyperLink 를 통해서 상태를 전파(전이) 시킬 수 있어야 한다.

* HTML의 경우 <a>태그를 통해서 다른 상태로 갈 수 있는 링크를 포함하고 있다. (HATEOAS를 만족한다)
    * List페이지 일경우 상세 페이지 url 혹은 Form url 
* JSON의 경우 기본적으로 Hyperlink를 통해서 상태를 전이 할 수 없다.(HATEOAS를 만족하지 않는다.)
    * Http Response Body에 link를 따로 추가적으로 삽입하여 해결할 수 있다. 
    * 이런 link를 만들어주는역활은 `spring-boot-starter-hateoas`를 통해 구현할 수 있다. 
    * 인증처리가 됬을때와 그렇지 않을때 혹은 페이지마다 표시되는 링크가 달라야 한다. 
    
HATEOAS가 적용되지 않은 JSON
```javascript
{
    "id": 1,
    "firstName": "test",
    "secondName": "one",
    "dateOfBirth": "01/01/0001 01:10",
    "profession": "im a test",
    "salary": 0
}
```

HATEOAS가 적용된 JSON
```javascript
{
	"person": {
		"id": 1,
		"firstName": "test",
		"secondName": "one",
		"dateOfBirth": "01/01/0001 01:10",
		"profession": "im a test",
		"salary": 0
	},
	"_links": {
		"people": {
			"href": "http://localhost:8090/people"
		},
		"memberships": {
			"href": "http://localhost:8090/people/1/memberships"
		},
		"self": {
			"href": "http://localhost:8090/people/1"
		}
	}
}
```

## JPA
Java ORM 기술에 대한 표준 명세 API
* 기본적인 쿼리를 자동으로 생성해준다. 
* Mybatis와 비교했을때 생산성이 뛰어나다. 
* JpaRepository<T, T> 상속만으로 CRUD관련 오퍼레이션을 사용가능.
* @Entity Annotation을 통해 Table과 Mapping하며 필요시 DTO클래스를 작성한다. 
* Pageable, PageRequest, Sort 를 통해서 paging & sort 가능  
* 쿼리에서 성능이슈가 생길경우 JPQL, QueryDSL을 사용가능 


## Test 
* JUnit 보다 조금더 간결한 API를 제공하는 AssertJ를 이용
* 슬라이싱 테스트를 할 때는 @WebMvcTest Annotation을 사용 할 수 있다. 
    * 단 슬라이싱 테스트를 할 경우 Controller를 제외한 Bean을 만들지 않는다. 
    * @MockBean과 같이 사용하거나 @SpringBootTest를 통해 모든 Bean을 생성하도록 한다. 
* API 작성하기 전에 Test를 작성 -> Test 실패 -> Test Pass하기 위한 코드를 작성 -> Test Pass를 반복 
* TDD를 통한 개발은 좀 더 개발에 대한 안정감을 높여준다.(물론 Test를 작성하기 나름)

## JsonSerializer 
* 기본적으로 Serialize를 지원하지 않는 Bean의 경우 JsonSerializer interface를 구현함으로써 해결할 수 있다.

## Lombok & ModelMapper
Java에서 DTO를 사용하거나 Entity클래스를 사용할때 getter, setter, constructor등 동일한 코드를 작성하는것은 소모적이다. 
그렇다고 Map을 이용하면 강타입의 장점을 살리지 못하는 느낌이다. 
lombok은 Annotation을 붙이는것으로 많은양의 중복코드를 대신 작성해준다. 
그리고 이렇게 만들어진 Entity클래스를 DTO로 변환할때 ModelMapper가 유용하게 사용된다. 

## spring-validation 
Annotation을 활용한 Field Level의 validation과 직접 custom validator를 통해 Domain Level Validation이 가능하다. 

