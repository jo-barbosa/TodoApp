# Guia de Integração React → Spring Boot API

Este guia simplifica a ligação do teu frontend React à API de Tarefas (Todo API).

## 🚀 Configuração Base da API

- **Base URL:** `http://localhost:8080`
- **CORS:** Já configurado na API para aceitar pedidos das portas `5173` (Vite) e `3000` (Create React App).

---

## 📋 Tabela de Endpoints

### Utilizadores (Users)
| Método | Endpoint | Descrição | Request Body | Status Sucesso |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/users` | Listar todos os utilizadores | *Nenhum* | `200 OK` |
| `GET` | `/api/users/{userId}` | Obter detalhes de um utilizador | *Nenhum* | `200 OK` |
| `POST` | `/api/users` | Criar/registar um utilizador | `{ "name": "...", "email": "..." }` | `201 Created` |

### Tarefas (Todos)
| Método | Endpoint | Descrição | Request Body | Status Sucesso |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/users/{userId}/todos` | Listar tarefas de um utilizador | *Nenhum* | `200 OK` |
| `GET` | `/api/users/{userId}/todos/{todoId}` | Obter tarefa específica | *Nenhum* | `200 OK` |
| `POST` | `/api/users/{userId}/todos` | Criar nova tarefa | `{ "description": "...", "dueDate": "YYYY-MM-DD" }` | `201 Created` |
| `PUT` | `/api/users/{userId}/todos/{todoId}` | Atualizar tarefa (conteúdo, estado, etc.) | `{ "description": "...", "completed": true, "dueDate": "YYYY-MM-DD" }` | `200 OK` |
| `DELETE` | `/api/users/{userId}/todos/{todoId}` | Eliminar tarefa | *Nenhum* | `204 No Content` |

---

## 🧪 Utilizadores de Teste (Mock Data)

Se iniciares a API com o perfil `bootstrap` (`./mvnw spring-boot:run -Dspring-boot.run.profiles=bootstrap`), podes usar diretamente estes utilizadores mockados para testar o teu frontend no React:

1. **João Barbosa**
   - **Email:** `joao@example.com`
   - **UUID de Teste:** Copiar o ID gerado nos logs no arranque da API (gerado aleatoriamente no bootstrap).
2. **Maria Silva**
   - **Email:** `maria@example.com`
   - **UUID de Teste:** Copiar o ID gerado nos logs no arranque da API.

---

## 🛠️ Exemplo de Cliente React (Axios / Fetch)

Aqui tens um exemplo completo em **TypeScript** de como estruturar as chamadas à API usando `fetch` (ou podes converter facilmente para `axios`).

```typescript
// src/services/api.ts

const API_BASE_URL = 'http://localhost:8080/api';

export interface User {
  id: string;
  name: string;
  email: string;
}

export interface Todo {
  id: string;
  description: string;
  completed: boolean;
  dueDate?: string; // Formato YYYY-MM-DD
  userId: string;
}

export interface CreateTodoRequest {
  description: string;
  dueDate?: string;
}

export interface UpdateTodoRequest {
  description: string;
  completed: boolean;
  dueDate?: string;
}

export const TodoApi = {
  // --- USERS ---
  async getAllUsers(): Promise<User[]> {
    const response = await fetch(`${API_BASE_URL}/users`);
    if (!response.ok) throw await handleApiError(response);
    return response.json();
  },

  async createUser(name: string, email: string): Promise<User> {
    const response = await fetch(`${API_BASE_URL}/users`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email }),
    });
    if (!response.ok) throw await handleApiError(response);
    return response.json();
  },

  // --- TODOS ---
  async getTodos(userId: string): Promise<Todo[]> {
    const response = await fetch(`${API_BASE_URL}/users/${userId}/todos`);
    if (!response.ok) throw await handleApiError(response);
    return response.json();
  },

  async createTodo(userId: string, todo: CreateTodoRequest): Promise<Todo> {
    const response = await fetch(`${API_BASE_URL}/users/${userId}/todos`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(todo),
    });
    if (!response.ok) throw await handleApiError(response);
    return response.json();
  },

  async updateTodo(userId: string, todoId: string, todo: UpdateTodoRequest): Promise<Todo> {
    const response = await fetch(`${API_BASE_URL}/users/${userId}/todos/${todoId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(todo),
    });
    if (!response.ok) throw await handleApiError(response);
    return response.json();
  },

  async deleteTodo(userId: string, todoId: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/users/${userId}/todos/${todoId}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw await handleApiError(response);
  }
};

// Estrutura de erro enviada pela API
export interface ApiErrorPayload {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  validationErrors?: Record<string, string>; // Erros por campo (ex: email: "Email inválido")
}

async function handleApiError(response: Response): Promise<Error & { payload?: ApiErrorPayload }> {
  try {
    const payload: ApiErrorPayload = await response.json();
    const error = new Error(payload.message || 'Erro na chamada à API') as any;
    error.payload = payload;
    return error;
  } catch {
    return new Error('Erro de ligação desconhecido.');
  }
}
```

---

## ⚠️ Tratamento de Erros de Validação no Frontend

Quando o utilizador submete dados inválidos (ex: e-mail inválido ao criar utilizador, ou descrição de tarefa em branco), a API responde com status `400 Bad Request` e um mapa de erros estruturado.

### Exemplo de Resposta de Erro da API (`400 Bad Request`):
```json
{
  "timestamp": "2026-07-17T10:47:00.123Z",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "path": "/api/users",
  "validationErrors": {
    "email": "Email must be a valid email address"
  }
}
```

No React, podes capturar esse erro e ligar o `validationErrors` diretamente ao estado de validação dos campos do formulário para mostrar mensagens de erro personalizadas no UI:

```tsx
const [errors, setErrors] = useState<Record<string, string>>({});

const handleSubmit = async () => {
  try {
    await TodoApi.createUser(name, email);
  } catch (err: any) {
    if (err.payload?.validationErrors) {
      // Define os erros de validação da API diretamente nos inputs
      setErrors(err.payload.validationErrors); 
    } else {
      alert(err.message);
    }
  }
};
```
