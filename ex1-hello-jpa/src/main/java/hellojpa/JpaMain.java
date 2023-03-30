package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 회원 등록
//            Member member = new Member();
//            member.setId(2L);
//            member.setName("HelloB");
//            em.persist(member);

           // 회원 조회
//            Member findMember = em.find(Member.class, 1L);
//            System.out.println("findMember.getId() = " + findMember.getId());
//            System.out.println("findMember.getName() = " + findMember.getName());

            // 회원 수정
//            findMember.setName("HelloJPA");
            // 회원 삭제
//            em.remove(findMember);

            // 전체 회원 조회
//            List<Member> result = em.createQuery("select m from Member as m", Member.class)
//                    .setFirstResult(5) // 5번부터
//                    .setMaxResults(10) // 10까지 가져와 -> 페이징 사용 가능
//                    .getResultList();
//
//            for (Member member : result) {
//                System.out.println("member.name = " + member.getName());
//            }


            // 비영속
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("HelloJPA");
//
//            // 영속
//            System.out.println("=== BEFORE ===");
//            em.persist(member); // transaction commit 할때 query가 나간다
//            System.out.println("=== AFTER ===");
//
//            Member findMember = em.find(Member.class, 101l); // 1차 캐시에 저장되었기 때문에 DB에 query를 날리지 않고, 1차 캐시에서 가져온다
//            System.out.println("findMember.id = " + findMember.getId());
//            System.out.println("findMember.name  = " + findMember.getName());

            // 준영속
//            em.detach(member);

            // 동일성 보장
//            Member findMember1 = em.find(Member.class, 101L);
//            Member findMember2 = em.find(Member.class, 101L);
//            System.out.println("result = " + (findMember1 == findMember2));

            // 트랜잭션을 지원하는 쓰기 지연
//            Member member1 = new Member(150L, "A");
//            Member member2 = new Member(160L, "B");
//
//            em.persist(member1);
//            em.persist(member2);
//            System.out.println("=============="); // 이 이후에 commit 시점에 query가 날라간다

            // 변경 감지 (Dirty Checking)
//            Member member = em.find(Member.class, 150L);
//            member.setName("Z");
//            System.out.println("==============");

            // 플러시 직접 호출
//            em.flush();

            // 특정 엔티티만 준영속 상태로 전환
//            em.detach(member);

            // 영속성 컨텍스트를 완전히 초기화
//            em.clear();

            // 영속성 컨텍스트를 종료
//            em.close();

            // 저장
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();

            for (Member m : members) {
                System.out.println("m = " + m.getUsername());
            }
            
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
