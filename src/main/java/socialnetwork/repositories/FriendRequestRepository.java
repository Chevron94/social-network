package socialnetwork.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import socialnetwork.entities.FriendRequest;

import java.util.List;

/**
 * Created by Roman on 05.08.2017.
 */
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    FriendRequest findFriendRequestBySender_IdAndReceiver_Id(Long sender, Long receiver);
    @Query(value = "select f from FriendRequest f where (f.sender.id = :userId1 and f.receiver.id = :userId2) " +
            "or (f.sender.id = :userId2 and f.receiver.id = :userId1)")
    FriendRequest findFriendRequestByTwoUserIds(@Param("userId1")Long userId1, @Param("userId2") Long userId2);
    List<FriendRequest> findFriendRequestBySender_IdAndConfirmedIsFalse(Long id, Pageable pageable);
    List<FriendRequest> findFriendRequestByReceiver_IdAndConfirmedIsFalse(Long id, Pageable pageable);
    Long countFriendRequestsBySender_Id(Long idUser);
    Long countFriendRequestsByReceiver_Id(Long idUser);
    @Query(value = "select f from FriendRequest f where (f.sender.id = :id or f.receiver.id = :id) and f.confirmed = true")
    List<FriendRequest> findFriendsByUser_Id(@Param("id") Long id, Pageable pageable);
    @Query(value = "select count (f) from FriendRequest f where (f.sender.id = :id or f.receiver.id = :id) and f.confirmed = true")
    Long countFriendsByUser_Id(@Param("id") Long id);
}
