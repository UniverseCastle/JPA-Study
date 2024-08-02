// 게시글 삭제 ajax
function deletePost(boardId) {
	$.ajaxSetup({
	    headers: {
	        'X-CSRF-TOKEN': $('input[name="_csrf"]').val() // CSRF 토큰
	    }
	});
	if (confirm('게시글을 정말 삭제하시겠습니까?')) {
		$.ajax({
			url: '/board/delete/' + boardId,
			type: 'DELETE',
			success: function(response) {
				alert('게시글이 삭제되었습니다.');
				location.href = '/board/list';
			},
			error: function(xhr, status, error) {
				alert('삭제 실패: ' + xhr.responseText);
			}
		});
	}
}

// 댓글 삭제 ajax
function deleteComment(commentId) {
	$.ajaxSetup({
	    headers: {
	        'X-CSRF-TOKEN': $('input[name="_csrf"]').val() // CSRF 토큰
	    }
	});
	if (confirm('댓글을 정말 삭제하시겠습니까?')) {
		$.ajax({
			url: '/comment/delete/' + commentId,
			type: 'DELETE',
			dataType: 'json', // 응답 데이터 타입 설정
			success: function(response, textStatus, xhr) {
				if (xhr.status == 204) { // 204 No Content
					// 댓글 삭제 성공
					alert('댓글이 삭제되었습니다.');
					$('#comment_' + commentId).remove(); // 댓글 DOM에서 제거
					updateCommentCount();
				}else {
					// 실패 처리
					alert('댓글 삭제 실패');
				}
			}
		});
	}
}

// 댓글 삭제 시 댓글 수 업데이트
function updateCommentCount() {
	// 현재 페이지에서 남아있는 댓글 수를 계산하여 업데이트
	const commentCount = $('#commentList').children().length; // 댓글 리스트의 자식 요소 수
    $('#commentCountDisplay').text(commentCount + '개의 댓글이 있습니다.'); // 댓글 수 업데이트
}