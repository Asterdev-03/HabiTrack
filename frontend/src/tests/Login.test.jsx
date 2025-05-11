import { fireEvent, render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import "@testing-library/jest-dom";
import Login from "../pages/Login";

function renderWithRouter(ui) {
  return render(<BrowserRouter>{ui}</BrowserRouter>);
}

test("renders login form and handles input", () => {
  renderWithRouter(<Login />);

  const usernameInput = screen.getByPlaceholderText(/Username/i);
  const passwordInput = screen.getByPlaceholderText(/Password/i);
  const loginButton = screen.getByRole("button", { name: /Login/i });

  expect(usernameInput).toBeInTheDocument();
  expect(passwordInput).toBeInTheDocument();
  expect(loginButton).toBeInTheDocument();

  fireEvent.change(usernameInput, { target: { value: "john" } });
  fireEvent.change(passwordInput, { target: { value: "1234" } });

  expect(usernameInput.value).toBe("john");
  expect(passwordInput.value).toBe("1234");
});
