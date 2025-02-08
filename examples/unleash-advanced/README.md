# Sample REST API to Test the Feature Flag with a Custom Strategy on User IDs

- **Request 1**  
  URL: `http://localhost:9999/feature/annotated/user/99`  
  Response: **User not allowed**

- **Request 2**  
  URL: `http://localhost:9999/feature/annotated/user/111`  
  Response: **User allowed**
